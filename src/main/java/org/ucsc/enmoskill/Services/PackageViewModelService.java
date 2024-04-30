package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.PackageViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PackageViewModelService {

    Connection con = null;
    ResultSet resultSet = null;
    int result = 0;
    PreparedStatement preparedStatement = null;

    String query = null;

    public PackageViewModel getData(int packageId, int designerId) {
        ResultSet resultSet1 = null;

        PreparedStatement preparedStatement1 = null;

        try {
            con = DatabaseConnection.initializeDatabase();

            if (con == null) {
                throw new RuntimeException("Failed to connect to database");
            }

            String query1 = "SELECT active_orders, user_ratings FROM designer_overview WHERE designerID=?;";

            preparedStatement1 = con.prepareStatement(query1);
            preparedStatement1.setInt(1, designerId);
            resultSet1 = preparedStatement1.executeQuery();


            PackageViewModel packageViewModel = new PackageViewModel();

            // Check if resultSet1 has any rows
            if (resultSet1.next()) {
                packageViewModel.setPendingOrders(resultSet1.getInt("active_orders"));
                packageViewModel.setUserRatings(resultSet1.getDouble("user_ratings"));
            } else {
                // Set default values if resultSet1 is empty
                packageViewModel.setPendingOrders(0);
                packageViewModel.setUserRatings(0.0);
            }


            return packageViewModel;
        } catch (SQLException e) {
            System.out.println("SQL Error executing query: " + e.getMessage());
            throw new RuntimeException("Failed to fetch package view model data", e);
        } finally {
            try {
                if (resultSet1 != null) resultSet1.close();
                if (preparedStatement1 != null) preparedStatement1.close();
                if (con != null) con.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateClickCount(int packageId){
        try {
            con = DatabaseConnection.initializeDatabase();

            if (con == null) {
                throw new RuntimeException("Failed to connect to database");
            }

            String query = "UPDATE package SET clicks = clicks + 1 WHERE package_id = ?;";

            System.out.println("clicks updated");
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, packageId);
            result = preparedStatement.executeUpdate();

            if (result == 0) {
                throw new RuntimeException("Failed to update click count");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error executing query: " + e.getMessage());
            throw new RuntimeException("Failed to update click count", e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
