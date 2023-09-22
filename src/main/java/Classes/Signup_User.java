package Classes;

import Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Signup_User {

    public static void signup(User user) throws SQLException {

        Connection connection = DatabaseConnectionManager.getConnection();
        String username = user.getUsername();
        String password = user.getPassword();

        String sqlQuery = "Insert into user(username, password) values(?, ?);";
        try{
            PreparedStatement ps = connection.prepareStatement(sqlQuery);

            ps.setString(1, username);
            ps.setString(2, password);

            int i = ps.executeUpdate();

            if (i>0){

                System.out.println("signup success");
            }
            else {
                System.out.println("error occured");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            // Make sure to close the connection when done
            DatabaseConnectionManager.closeConnection(connection);
        }

    }
}
