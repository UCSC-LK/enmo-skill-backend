package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.ucsc.enmoskill.Services.BannerDesDeliverablesService.getBDDeliverables;
import static org.ucsc.enmoskill.Services.FlyerDesDeliverablesService.getFDDeliverables;
import static org.ucsc.enmoskill.Services.IllustrationDeliverablesService.getIllusDeliverables;
import static org.ucsc.enmoskill.Services.LogoDesDeliverablesService.getLDDeliverables;

public class PricePackageService {

    public static StringBuilder fetchData(int packageId, int category){
        List<PackagePricing> pricingList;
        pricingList = getPricePackage(packageId);


        // fetch the pricing details
        if (!pricingList.isEmpty()) {

            System.out.println(pricingList.size());

            StringBuilder jsonObj = new StringBuilder("[");

            for (PackagePricing pricing : pricingList) {
                int pricePackageId = pricing.getPricePackageId();

                System.out.println(pricePackageId);
                Gson gson = new Gson();

                // Convert pricing object to JSON
                String jsonPricing = gson.toJson(pricing);
                String jsonDeliverables = null;


                switch (category) {
                    case 1:
                        LogoDesignDeliverables deliverables1 = getLDDeliverables(pricePackageId);

                        // Convert deliverables object to JSON
                        jsonDeliverables = gson.toJson(deliverables1);

                        break;

                    case 2:
                        IllustrationDeliverables deliverables2 = getIllusDeliverables(pricePackageId);
                        // Convert deliverables object to JSON
                        jsonDeliverables = gson.toJson(deliverables2);
                        break;

                    case 3:
                        FlyerDesignDeliverables deliverables3 = getFDDeliverables(pricePackageId);
                        // Convert deliverables object to JSON
                        jsonDeliverables = gson.toJson(deliverables3);
                        break;

                    default:
                        BannerDesignDeliverables deliverables4 = getBDDeliverables(pricePackageId);
                        // Convert deliverables object to JSON
                        jsonDeliverables = gson.toJson(deliverables4);
                        break;
                }


                // Create a JSON object for pricing
                StringBuilder jsonResult = new StringBuilder(jsonPricing);

                // Add a new field for deliverables within the pricing JSON object
                jsonResult.insert(jsonResult.length() - 1, ", \"deliverables\":" + jsonDeliverables);

                jsonObj.append(jsonResult);
                jsonObj.append(",");
//                System.out.println(jsonResult);

            }

            int lastIndex = jsonObj.length() - 1;
            jsonObj.deleteCharAt(lastIndex);
            jsonObj.append("]");

//            System.out.println(jsonObj);
            return jsonObj;

        } else{
            return null;
        }
    }

    public static float getBronzePrice(int packageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        float result = 0.0F;

        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "SELECT price FROM package_pricing WHERE type='bronze' AND package_id=?;";

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, packageId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getFloat("price");
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static float getPlatinumPrice(int packageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        float result = 0.0F;

        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "SELECT price FROM package_pricing WHERE type='platinum' AND package_id=?;";

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, packageId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getFloat("price");
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public  static int updatePricePackageData(PackagePricing newPackagePricing){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "UPDATE package_pricing SET type=?, delivery_duration=?, no_of_revisions=?, price=?, no_of_concepts=?, package_id=? WHERE price_package_id=?;";

            preparedStatement = con.prepareStatement(query);

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, newPackagePricing.getType());
            preparedStatement.setString(2, newPackagePricing.getDeliveryDuration());
            preparedStatement.setString(3, newPackagePricing.getNoOfRevisions());
            preparedStatement.setFloat(4, newPackagePricing.getPrice());
            preparedStatement.setInt(5, newPackagePricing.getNoOfConcepts());
            preparedStatement.setInt(6, newPackagePricing.getPackageId());
            preparedStatement.setInt(7, newPackagePricing.getPricePackageId());

            result = preparedStatement.executeUpdate();

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions during closing connections if needed
            }
        }
    }

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

    public static List<PackagePricing> getPricePackage(int packageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT price_package_id, type, delivery_duration, no_of_revisions, price, no_of_concepts, package_id FROM package_pricing WHERE package_id = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, packageId);

            resultSet = preparedStatement.executeQuery();

            List<PackagePricing> packagePricings = new ArrayList<>();

            while (resultSet.next()){
                PackagePricing newPackagePricing = new PackagePricing(resultSet.getInt("price_package_id"),
                resultSet.getString("type"), resultSet.getString("delivery_duration"),
                resultSet.getString("no_of_revisions"), resultSet.getFloat("price"),
                resultSet.getInt("no_of_concepts"), resultSet.getInt("package_id"));

                packagePricings.add(newPackagePricing);



            }

            return packagePricings;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        // Close the database connections in a finally block
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions during closing connections if needed
        }
    }
    }
}
