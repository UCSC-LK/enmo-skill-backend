package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.AdminDashboardModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

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
}
