package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginSer {
    public static Login getLoginData(Login login) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT username, password FROM users WHERE username=?";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, login.getUsername());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");

                // Create a new Login object with the retrieved username and stored hashed password
                return new Login(resultSet.getString("username"), storedPassword);
            }else {
                // If the username is not found, return a special Login object or null
                return new Login("NotFound", ""); // You can customize this object accordingly
            }
        } finally {
            // Close the resultSet, preparedStatement, and connection in a finally block
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }
}
