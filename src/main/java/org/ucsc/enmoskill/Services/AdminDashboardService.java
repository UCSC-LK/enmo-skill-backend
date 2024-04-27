package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.AdminDashboardModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminDashboardService {

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    int result = 0;

    public AdminDashboardModel getDashboardData() {

        // make required objects
        AdminDashboardModel adminDashboardModel = new AdminDashboardModel();
        adminDashboardModel.setCategoryAnalytics(new HashMap<>());
        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT category, COUNT(order_id) AS order_count FROM order_package_view GROUP BY category ORDER BY order_count DESC LIMIT 4";
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                adminDashboardModel.getCategoryAnalytics().put(resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminDashboardModel;
    }

    // get user count
    public int getUserCount() {
        int userCount = 0;
        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT COUNT(userID) FROM users";
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userCount = resultSet.getInt(1);
                System.out.println(userCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userCount;
    }

    public int getPackageCount() {
        int packageCount = 0;
        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT COUNT(package_id) FROM package";
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                packageCount = resultSet.getInt(1);
                System.out.println(packageCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return packageCount;
    }

    public double getTotalEarnings(){
        Double res = 0.0;
        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT SUM(total_earnings) FROM designer_overview;";
//            String query = "SELECT SUM(e.price-e.price*c.percentage) FROM earnings e, platform_charge_rates c WHERE c.charge_category=e.platform_charge_id;";
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                res = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public HashMap<Date, Double>  getOrderData() {

        HashMap<Date, Double> date_orders = new HashMap<>();

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT DATE(created_time) AS date, SUM(price) AS earnings " +
                    "FROM orders " +
                    "WHERE MONTH(created_time) = 4 " +
                    "GROUP BY DATE(created_time) " +
                    "ORDER BY created_time ";
            preparedStatement = con.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                date_orders.put(resultSet.getDate(1), resultSet.getDouble(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date_orders;
    }
}
