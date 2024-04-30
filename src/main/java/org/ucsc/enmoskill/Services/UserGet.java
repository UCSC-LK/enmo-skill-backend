package org.ucsc.enmoskill.Services;

import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserGet {

    String userID;

    public UserGet(TokenService.TokenInfo tokenInfo) {
        userID = tokenInfo.getUserId();
    }
    public UserGet(String userID) {
        this.userID = userID;
    }

    public ResponsModel Run()  {
        Connection connection = DatabaseConnection.initializeDatabase();
        if (connection == null) {
            return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        String query = "SELECT username,email,contact_no,url,name FROM users WHERE userid ="+ userID;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();

        String query2 = "SELECT joinedDate,country FROM client WHERE userid ="+ userID;
        PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
        ResultSet resultSet2 = preparedStatement2.executeQuery();
            String username = null,email = null,contact_no = null,url = null,name = null,joinedDate = null,country = null;
            if(resultSet.next()){
                username = resultSet.getString("username");
                email = resultSet.getString("email");
                contact_no = resultSet.getString("contact_no");
                url = resultSet.getString("url");
                name = resultSet.getString("name");
            }
            if (resultSet2.next()){
                joinedDate = resultSet2.getString("joinedDate");

                country = resultSet2.getString("country");
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("username", username);
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("contact_no", contact_no);
            jsonObject.addProperty("url", url);
            jsonObject.addProperty("name", name);
            jsonObject.addProperty("joinedDate", joinedDate);
            jsonObject.addProperty("country", country);
            return new ResponsModel(jsonObject.toString(), HttpServletResponse.SC_OK);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
