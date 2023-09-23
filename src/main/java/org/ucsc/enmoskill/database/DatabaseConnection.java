package org.ucsc.enmoskill.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection initializeDatabase() throws SQLException, ClassNotFoundException{

        String dbDriver = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3310/";

        String dbName = "enmoskill";
        String dbUsername = "root";
        String dbPassword = "Udee1234@#";

        Class.forName(dbDriver);
        Connection con = DriverManager.getConnection(dbURL + dbName, dbUsername , dbPassword);
        return con;
    }
}
