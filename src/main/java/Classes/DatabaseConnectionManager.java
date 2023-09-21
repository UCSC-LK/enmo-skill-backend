package Classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/enmo_database";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "hpljp1102";
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("classname");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("JDBC Driver not found. Include the MySQL JDBC driver in your project's classpath.");
        }
    }
    public static Connection getConnection() {
        Connection connection = null;
        try {

            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
//            System.out.println("connection");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.toString());
            // Handle database connection errors here
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
