package Classes;

import Models.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Validate_user {
    public static boolean Validate(User user){
        Connection connection = DatabaseConnectionManager.getConnection();
        String username = user.getUsername();
        String sqlQuery = "SELECT username, password FROM user WHERE username = '" + username + "'";

        try {
            ResultSet resultSet = connection.prepareStatement(sqlQuery).executeQuery();

            // Check if there is at least one row in the result set
            if (resultSet.next()) {
                // Move the cursor to the first row
                String password = resultSet.getString("password");
                System.out.println(password);
                if(user.getPassword().equals(password)){
                    return true;
                }else {
                    return false;
                }
            } else {
                // Handle the case where no rows were found
                System.out.println("No user found with the username "+username+".");
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;

        }
//        finally {
//            // Make sure to close the connection when done
////            DatabaseConnectionManager.closeConnection(connection);
//        }
    }
}
