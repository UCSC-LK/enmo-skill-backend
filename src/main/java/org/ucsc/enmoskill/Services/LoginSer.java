package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginSer {

    public  Login getLoginData(Login login) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT users.username, users.password,users.email,users.userID, user_level_mapping.userlevelID FROM users inner join user_level_mapping on users.userID=user_level_mapping.userID WHERE email= ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, login.getEmail());

            resultSet = preparedStatement.executeQuery();

            System.out.println("results: " + resultSet);
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");

                System.out.println("results: " + storedPassword);
                // Create a new Login object with the retrieved username and stored hashed password
                return new Login(resultSet.getString("userID"),resultSet.getString("email"),resultSet.getString("username"),storedPassword,resultSet.getString("userlevelID"));
            }else {
                // If the username is not found, return a special Login object or null
                return new Login("NotFound", "", "","",""); // You can customize this object accordingly
            }
        } finally {
            // Close the resultSet, preparedStatement, and connection in a finally block
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }

        }

    }
}
