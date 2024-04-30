package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class ClientDetailsPUT {
    private HttpServletResponse response;
    private User data;

    public ClientDetailsPUT(HttpServletResponse response, User data) {
        this.response = response;
        this.data = data;
    }
    public ClientDetailsPUT(User data){
        this.data =data;
    }

    public void Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            response.getWriter().write("SQL Connection Error");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        try {
            String userQuery = data.getInsertUserDetails();


            PreparedStatement massageStatement = connection.prepareStatement(userQuery);
            int affectedRows = massageStatement.executeUpdate();

            if (affectedRows == 0) {
                response.getWriter().write("Inserting message failed, no rows affected.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new SQLException("Inserting userdata failed, no rows affected.");
            }else {
                System.out.println("Successfully updated!");
                response.getWriter().write("Successfully updated!");
                response.setStatus(HttpServletResponse.SC_CREATED);
            }



        } catch (SQLException e) {
            response.getWriter().write(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
    public ResponsModel SaveUser() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            return new ResponsModel("SQL Connection Error",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }



        try {

            String retrieveQuery = String.format("SELECT userid FROM users WHERE username = '%s';", data.getUsername());
            PreparedStatement statement = connection.prepareStatement(retrieveQuery);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("userid");
                data.setId(userId);
                System.out.println("User ID: " + userId);
            } else {
                System.out.println("User not found!");
                return new ResponsModel("User not found!",HttpServletResponse.SC_NOT_FOUND);
            }


            String userQuery = String.format("UPDATE users SET name = '%s' WHERE userID = %s;",data.getName(),data.getId());

            String clientQuery = String.format("INSERT INTO client (userid, joinedDate, country) VALUES (%s,  CURDATE(), '%s');",data.getId(),data.getCountry());


            PreparedStatement massageStatement = connection.prepareStatement(userQuery);
            int affectedRows = massageStatement.executeUpdate();

            if (affectedRows == 0) {
                return new ResponsModel("Inserting userdata failed, no rows affected.",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            PreparedStatement chatMappingStatement = connection.prepareStatement(clientQuery);
            int ClientdataAffected = chatMappingStatement.executeUpdate();
            if (ClientdataAffected == 0) {
                return new ResponsModel("Inserting client data failed, no rows affected.",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            }
            else {
                System.out.println("Successfully updated!");
                return new ResponsModel("Successfully updated!",HttpServletResponse.SC_CREATED);
            }


        } catch (SQLException e) {
            return new ResponsModel(e.toString(),HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
}
