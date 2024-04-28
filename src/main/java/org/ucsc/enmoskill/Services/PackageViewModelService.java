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
        ResultSet resultSet2 = null;

        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;

        try {
            con = DatabaseConnection.initializeDatabase();

            if (con == null) {
                throw new RuntimeException("Failed to connect to database");
            }

            String query1 = "SELECT active_orders, user_ratings FROM designer_overview WHERE designerID=?;";
            String query2 = "SELECT AVG(stars) FROM review WHERE package_id=? GROUP BY package_id;";

            preparedStatement1 = con.prepareStatement(query1);
            preparedStatement1.setInt(1, designerId);
            resultSet1 = preparedStatement1.executeQuery();

            preparedStatement2 = con.prepareStatement(query2);
            preparedStatement2.setInt(1, packageId);
            resultSet2 = preparedStatement2.executeQuery();

            PackageViewModel packageViewModel = new PackageViewModel();

            if (resultSet1.next() && resultSet2.next()) {
                packageViewModel.setPendingOrders(resultSet1.getInt("active_orders"));
                packageViewModel.setUserRatings(resultSet1.getDouble("user_ratings"));
                packageViewModel.setPackageRatings(resultSet2.getDouble(1)); // Index 1 is used since it's the only column in the ResultSet
                return packageViewModel;
            }

            return null;
        } catch (SQLException e) {
            System.out.println("SQL Error executing query: " + e.getMessage());
            throw new RuntimeException("Failed to fetch package view model data", e);
        } finally {
            try {
                if (resultSet1 != null) resultSet1.close();
                if (resultSet2 != null) resultSet2.close();
                if (preparedStatement1 != null) preparedStatement1.close();
                if (preparedStatement2 != null) preparedStatement2.close();
                if (con != null) con.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
