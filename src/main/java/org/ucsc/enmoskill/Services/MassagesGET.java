package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ChatModel;
import org.ucsc.enmoskill.model.MessageModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MassagesGET {
    HttpServletResponse response;
    String userid;
    String chatid;

    public MassagesGET(HttpServletResponse response, String userid, String chatid) {
        this.response = response;
        this.userid = userid;
        this.chatid = chatid;
    }
    public void Run() throws SQLException, IOException {
        String query ="SELECT " +
                "m.content," +
                "m.time," +
                "cm.receiver AS receiver_id " +
                "FROM chat_mapping cm " +
                "INNER JOIN massage m ON cm.msg_id = m.msg_id " +
                "INNER JOIN chat c ON cm.chat_id = c.chat_id " +
                "WHERE c.chat_id = " + chatid+
                " AND (c.user_1 = "+userid+" OR c.user_2 = "+userid+") " +
                "ORDER BY m.time ASC;";
        Connection connection = DatabaseConnection.initializeDatabase();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        JsonArray jsonArray = new JsonArray();
        while (resultSet.next()){
            MessageModel messageModel = new MessageModel(resultSet);
            JsonObject jsonObject=new Gson().toJsonTree(messageModel).getAsJsonObject();
            jsonObject.addProperty("isyour",messageModel.isYours(userid));
            jsonArray.add(jsonObject);

        }
        response.getWriter().write(jsonArray.toString());
    }
}
