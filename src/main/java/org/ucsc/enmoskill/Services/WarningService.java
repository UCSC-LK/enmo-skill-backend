package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.WarningModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarningService {

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    int result = 0;

    public int insertWarning(WarningModel warning) {

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO warning (reason, user_id, ticket_id) VALUES (?, ?, ?);";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, warning.getReason());
            preparedStatement.setInt(2, warning.getUserId());
            preparedStatement.setInt(3, warning.getTicketId());
            result = preparedStatement.executeUpdate();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error executing query: " + e.getMessage());
            throw new RuntimeException("Failed to fetch dashboard data", e);
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

    public List<WarningModel> getWarnings(int userId){
        con = DatabaseConnection.initializeDatabase();
        String query = "SELECT * FROM warning WHERE user_id = ?;";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            List<WarningModel> list = new ArrayList<>();

            if (resultSet != null){
                WarningModel warningModel = new WarningModel();
                while (resultSet.next()){
                    warningModel.setWarningId(resultSet.getInt("warning_id"));
                    warningModel.setReason(resultSet.getString("reason"));
                    warningModel.setDate(resultSet.getDate("date"));
                    warningModel.setUserId(resultSet.getInt("user_id"));
                    warningModel.setTicketId(resultSet.getInt("ticket_id"));

                    list.add(warningModel);
                }
                return list;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error executing query: " + e.getMessage());
            throw new RuntimeException("Failed to fetch dashboard data", e);
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
