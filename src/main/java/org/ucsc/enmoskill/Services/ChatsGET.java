package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ChatModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatsGET {
    HttpServletResponse response;
    String userid;

    public ChatsGET(HttpServletResponse response, String userid) {
        this.response = response;
        this.userid = userid;
    }

    public void Run() throws SQLException, IOException {
     String query ="SELECT"+
             " CASE" +
             " WHEN c.user_1 = u.userid THEN u2.username" +
             " ELSE u1.username" +
             " END AS other_user_username," +
             " CASE" +
             " WHEN c.user_1 = u.userid THEN u2.url "+
             " ELSE u1.url "+
             " END AS other_user_url," +
             " cm.chat_id AS chat_id,"+
             " m.content AS last_message_content " +
             " FROM chat c" +
             " INNER JOIN chat_mapping cm ON c.chat_id = cm.chat_id" +
             " INNER JOIN massage m ON cm.msg_id = m.msg_id" +
             " INNER JOIN users u ON u.userid = " + userid +
             " INNER JOIN users u1 ON c.user_1 = u1.userid" +
             " INNER JOIN users u2 ON c.user_2 = u2.userid" +
             " WHERE u.userid IN (c.user_1, c.user_2)" +
             " AND cm.msg_id = (" +
             " SELECT MAX(msg_id)" +
             " FROM chat_mapping" +
             " WHERE chat_id = c.chat_id" +
             ")" +
             " ORDER BY m.time DESC;";
     Connection connection = DatabaseConnection.initializeDatabase();
     PreparedStatement preparedStatement = connection.prepareStatement(query);
     ResultSet resultSet = preparedStatement.executeQuery();
     JsonArray jsonArray = new JsonArray();
     while (resultSet.next()){
         ChatModel chatModel = new ChatModel(resultSet);
         JsonObject jsonObject=new Gson().toJsonTree(chatModel).getAsJsonObject();
         jsonArray.add(jsonObject);

     }
     response.getWriter().write(jsonArray.toString());
 }

}
