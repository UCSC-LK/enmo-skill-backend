package org.ucsc.enmoskill.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


    static Dotenv dotenv = Dotenv.load();

    String dbURL =dotenv.get("DB_URL");
    private static  Connection connection= null;
    private static final String JDBC_URL = dotenv.get("DB_URL");
    private static final String dbName = dotenv.get("DB_NAME");
    private static final String DB_USER = dotenv.get("DB_USERNAME");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
           //System.out.println("classname");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("JDBC Driver not found. Include the MySQL JDBC driver in your project's classpath.");
        }

        try {
            connection = DriverManager.getConnection(JDBC_URL+dbName, DB_USER, DB_PASSWORD);
            System.out.println("connection main");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }

    public static Connection initializeDatabase() {
        Boolean conection_status;
        try {
            conection_status = connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(connection==null||conection_status) {
            try {

                connection = DriverManager.getConnection(JDBC_URL+dbName, DB_USER, DB_PASSWORD);
                //System.out.println("connection function new created");
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(e.toString());
                return null;
            }

        }else {
           //System.out.println("connection function old");
            return connection;

        }

    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                //System.out.println("connection is closed :"+connection.isClosed());
                connection.close();
                //System.out.println("connection is closed :"+connection.isClosed());

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}