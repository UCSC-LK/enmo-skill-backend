package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.MessageModel;
import org.ucsc.enmoskill.model.NotificationModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationGET {
    TokenService.TokenInfo tokenInfo;
    HttpServletResponse response;

    public NotificationGET(HttpServletResponse response, TokenService.TokenInfo tokenInfo) {
        this.response = response;
        this.tokenInfo = tokenInfo;
    }
    public void Run() {
        String query ="SELECT t.*  FROM enmo_database.notifications t WHERE userId = "+tokenInfo.getUserId()+" ORDER BY date desc LIMIT 50";
        Connection connection = DatabaseConnection.initializeDatabase();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            JsonArray jsonArray = new JsonArray();
            int count = 0;
            while (resultSet.next()) {
                NotificationModel notificationModel = new NotificationModel(resultSet);
                JsonObject jsonObject = new Gson().toJsonTree(notificationModel).getAsJsonObject();
                jsonArray.add(jsonObject);
                count++;

            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("count",count);
            jsonObject.add("notifications",jsonArray);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
