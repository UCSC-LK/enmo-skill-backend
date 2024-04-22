package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BannModel;
import org.ucsc.enmoskill.model.WarningModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BannService {


    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    int result = 0;


    public int insertBann(BannModel bann) {

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO bann (reason, user_id, ticket_id) VALUES (?, ?, ?);";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, bann.getReason());
            preparedStatement.setInt(2, bann.getUserId());
            preparedStatement.setInt(3, bann.getTicketId());
            result = preparedStatement.executeUpdate();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error executing query: " + e.getMessage());
            throw new RuntimeException("Failed to insert data", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

    }
}
