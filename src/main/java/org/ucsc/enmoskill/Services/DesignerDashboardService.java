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


//    public DesignerDashboardModel getData(int designerId){
//        con = DatabaseConnection.initializeDatabase();
//
//        query = "SELECT o.designerId, o.pending_orders, o.cancelled_orders, o.completed_orders, o.total_earnings, o.user_ratings, u.url  FROM designer_overview o, users u WHERE o.designerID = u.userID AND designerID = ?;";
//        try {
//            preparedStatement = con.prepareStatement(query);
//            preparedStatement.setInt(1, designerId);
//            resultSet = preparedStatement.executeQuery();
//
//            if (resultSet != null){
//                DesignerDashboardModel dashboardModel = new DesignerDashboardModel();
//
//                while (resultSet.next()){
//                    dashboardModel.setCompletedOrders(resultSet.getInt("completed_orders"));
//                    dashboardModel.setPendingOrders(resultSet.getInt("pending_orders"));
//                    dashboardModel.setCancelledOrders(resultSet.getInt("cancelled_orders"));
//                    dashboardModel.setTotalEarnings(resultSet.getInt("total_earnings"));
//                    dashboardModel.setDesignerId(resultSet.getInt("designerId"));
//                    dashboardModel.setUserRatings(resultSet.getDouble("user_ratings"));
//                    dashboardModel.setProfileImg(resultSet.getString("url"));
//
//                }
//
//                return dashboardModel;
//            }
//            return null;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public DesignerDashboardModel getData(int designerId) {
        con = DatabaseConnection.initializeDatabase();

        if (con == null) {
            throw new RuntimeException("Failed to connect to database");
        }

//        query = "SELECT o.designerId, o.pending_orders, o.cancelled_orders, o.completed_orders, o.total_earnings, o.user_ratings, u.url  FROM designer_overview o, users u WHERE o.designerID = u.userID AND designerID = ?;";
        query = "SELECT o.designerId, o.active_orders, o.cancelled_orders, o.completed_orders, o.total_earnings, o.user_ratings, u.url FROM users u LEFT JOIN designer_overview o ON o.designerID = u.userID AND o.designerID = ?";


        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, designerId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                DesignerDashboardModel dashboardModel = new DesignerDashboardModel();

                while (resultSet.next()) {
                    dashboardModel.setCompletedOrders(resultSet.getInt("completed_orders"));
                    dashboardModel.setPendingOrders(resultSet.getInt("active_orders"));
                    dashboardModel.setCancelledOrders(resultSet.getInt("cancelled_orders"));
                    dashboardModel.setTotalEarnings(resultSet.getInt("total_earnings"));
                    dashboardModel.setDesignerId(resultSet.getInt("designerId"));
                    dashboardModel.setUserRatings(resultSet.getDouble("user_ratings"));
                    dashboardModel.setProfileImg(resultSet.getString("url"));
                }

                return dashboardModel;
            }
            return null;
        } catch (SQLException e) {
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
