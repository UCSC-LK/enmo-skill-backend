package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.PackagePricing;

import java.sql.*;

public class PricePackageService {

    public static int insertPricePackageData(PackagePricing newPackagePricing){

        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

//        package_pricing(price_package_id, type, delivery_duration, no_of_revisions, price, no_of_concepts, packageID)
        try {
            con = DatabaseConnection.initializeDatabase();

            String query = "INSERT INTO package_pricing (type, delivery_duration, no_of_revisions, price, no_of_concepts, package_id) values(?,?,?,?,?,?);";
            preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, newPackagePricing.getType());
            preparedStatement.setString(2, newPackagePricing.getDeliveryDuration());
            preparedStatement.setString(3, newPackagePricing.getNoOfRevisions());
            preparedStatement.setFloat(4, newPackagePricing.getPrice());
            preparedStatement.setInt(5, newPackagePricing.getNoOfConcepts());
            preparedStatement.setInt(6, newPackagePricing.getPackageId());


            result = preparedStatement.executeUpdate();

            if (result > 0) {
                // Retrieve the auto-generated keys (including the primary key)
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);

                    } else {
                        throw new SQLException("Creating price package failed, no ID obtained.");
                    }
                }
            }

            return result;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
