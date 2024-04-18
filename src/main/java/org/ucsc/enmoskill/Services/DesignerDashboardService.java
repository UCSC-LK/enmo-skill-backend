package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.DesignerDashboardModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DesignerDashboardService {

    Connection con = null;
    ResultSet resultSet = null;
    int result = 0;
    PreparedStatement preparedStatement = null;

    String query = null;


    public DesignerDashboardModel getData(int designerId){
        con = DatabaseConnection.initializeDatabase();

        query = "SELECT * FROM designer_overview WHERE designerID = ?;";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, designerId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
                DesignerDashboardModel dashboardModel = new DesignerDashboardModel();

                while (resultSet.next()){
                    dashboardModel.setCompletedOrders(resultSet.getInt("completed_orders"));
                    dashboardModel.setPendingOrders(resultSet.getInt("pending_orders"));
                    dashboardModel.setTotalEarnings(resultSet.getInt("total_earnings"));
                    dashboardModel.setDesignerId(resultSet.getInt("designerId"));
                    dashboardModel.setUserRatings(resultSet.getDouble("user_ratings"));

                }

                return dashboardModel;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
