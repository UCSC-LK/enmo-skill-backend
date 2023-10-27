package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UserTable {

    public static boolean isInsertionSuccessful(User user) {
        // You can implement logic here to check if the insertion was successful
        // For example, you can return true if no exceptions were thrown during insertion
        // and false if an exception occurred.

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO users (email,username, password ) VALUES (?, ?, ?)";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate(); // Execute the INSERT operation

            // If the execution reaches this point, the insertion was successful
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            // Handle any exceptions that might occur during the insertion
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
