package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.ucsc.enmoskill.Services.BannerDesDeliverablesService.getBDDeliverables;
import static org.ucsc.enmoskill.Services.FlyerDesDeliverablesService.getFDDeliverables;
import static org.ucsc.enmoskill.Services.IllustrationDeliverablesService.getIllusDeliverables;
import static org.ucsc.enmoskill.Services.LogoDesDeliverablesService.getLDDeliverables;

public class PricePackageService {

    public PackagePricing getapricePackage(int pricePackageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            query = "SELECT * FROM package_pricing WHERE price_package_id = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, pricePackageId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
                PackagePricing pricing = new PackagePricing();

                while (resultSet.next()){
                    pricing.setPricePackageId(resultSet.getInt("price_package_id"));
                    pricing.setType(resultSet.getString("type"));
                    pricing.setPrice(resultSet.getInt("price"));
                    pricing.setPackageId(resultSet.getInt("package_id"));
                    pricing.setDeliveryDuration(resultSet.getString("delivery_duration"));
                    pricing.setNoOfRevisions(resultSet.getString("no_of_revisions"));
                    pricing.setNoOfConcepts(resultSet.getInt("no_of_concepts"));

                }
                return pricing;
            }
            return null;

        } catch (Exception e) {
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
    public HashMap<String, Integer> createHashMap(DeliverablesModel deliverables){
        
        // hashmap object
        HashMap<String, Integer> delMap = new HashMap<String, Integer>();
        
        //get the relevant category details
//        DeliverablesModel delModel = pricing.getDel();
        DesignCategoryService newService = new DesignCategoryService();

        DesignCategoryModel model = newService.getCategory(deliverables.getCategoryId());

        delMap.put(model.getDel_1(), deliverables.getDel_1());
        delMap.put(model.getDel_2(), deliverables.getDel_2());
        delMap.put(model.getDel_3(), deliverables.getDel_3());
        delMap.put(model.getDel_4(), deliverables.getDel_4());
        delMap.put(model.getDel_5(), deliverables.getDel_5());


        return delMap;
        
    }

    public List<PackagePricing> fetchData(int packageId){
        List<PackagePricing> pricingList;
        pricingList = getPricePackage(packageId);

        Gson gson = new Gson();

        // fetch the pricing details
        if (!pricingList.isEmpty()) {

            return pricingList;

        } else{
            return null;
        }
    }

//    public static float getBronzePrice(int packageId){
//        Connection con = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        float result = 0.0F;
//
//        try{
//            con = DatabaseConnection.initializeDatabase();
//
//            String query = "SELECT price FROM package_pricing WHERE type='bronze' AND package_id=?;";
//
//            preparedStatement = con.prepareStatement(query);
//            preparedStatement.setInt(1, packageId);
//
//            resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                result = resultSet.getFloat("price");
//            }
//
//            return result;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static float getPlatinumPrice(int packageId){
//        Connection con = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        float result = 0.0F;
//
//        try{
//            con = DatabaseConnection.initializeDatabase();
//
//            String query = "SELECT price FROM package_pricing WHERE type='platinum' AND package_id=?;";
//
//            preparedStatement = con.prepareStatement(query);
//            preparedStatement.setInt(1, packageId);
//
//            resultSet = preparedStatement.executeQuery();
//
//            if (resultSet.next()) {
//                result = resultSet.getFloat("price");
//            }
//
//            return result;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }



    public int updatePricePackageData(PackagePricing newPackagePricing){
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

    public int insertPricePackageData(PackagePricing newPackagePricing){

        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

//
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

    public List<PackagePricing> getPricePackage(int packageId) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            query = "SELECT * FROM package_pricing pp LEFT JOIN price_package_deliverables pd "
                    + "ON pp.price_package_id = pd.price_package_id WHERE package_id = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,packageId);
            resultSet = preparedStatement.executeQuery();

            List<PackagePricing> packagePricings = new ArrayList<>();

            while (resultSet.next()) {

                DeliverablesModel newDeliverables = new DeliverablesModel();
                newDeliverables.setCategoryId(resultSet.getInt("category"));
                newDeliverables.setDeliverablesId(resultSet.getInt("deliverables_id"));
                newDeliverables.setPricePackageId(resultSet.getInt("price_package_id"));
                newDeliverables.setDel_1(resultSet.getInt("del_1"));
                newDeliverables.setDel_2(resultSet.getInt("del_2"));
                newDeliverables.setDel_3(resultSet.getInt("del_3"));
                newDeliverables.setDel_4(resultSet.getInt("del_4"));
                newDeliverables.setDel_5(resultSet.getInt("del_5"));

                // create the hashmap
                HashMap<String, Integer> delMap = createHashMap(newDeliverables);

                //Designcategory model
                DesignCategoryService categoryService = new DesignCategoryService();
                DesignCategoryModel categoryModel = categoryService.getCategory(newDeliverables.getCategoryId());

                PackagePricing newPackagePricing = new PackagePricing();
                newPackagePricing.setPricePackageId(resultSet.getInt("price_package_id"));
                newPackagePricing.setType(resultSet.getString("type"));
                newPackagePricing.setDeliveryDuration(resultSet.getString("delivery_duration"));
                newPackagePricing.setNoOfRevisions(resultSet.getString("no_of_revisions"));
                newPackagePricing.setPrice(resultSet.getFloat("price"));
                newPackagePricing.setNoOfConcepts(resultSet.getInt("no_of_concepts"));
                newPackagePricing.setPackageId(resultSet.getInt("package_id"));
                newPackagePricing.setDel(newDeliverables);
//                newPackagePricing.setDelMap(delMap);
//                newPackagePricing.setCriteria(categoryModel);


                packagePricings.add(newPackagePricing);


            }

            return packagePricings;


//        try {
//            con = DatabaseConnection.initializeDatabase();
//            query = "SELECT price_package_id, type, delivery_duration, no_of_revisions, price, no_of_concepts, package_id FROM package_pricing WHERE package_id = ?;";
//            preparedStatement = con.prepareStatement(query);
//            preparedStatement.setInt(1, packageId);
//
//            resultSet = preparedStatement.executeQuery();
//
//            List<PackagePricing> packagePricings = new ArrayList<>();
//
//            while (resultSet.next()) {
////                PackagePricing newPackagePricing = new PackagePricing(resultSet.getInt("price_package_id"),
////                resultSet.getString("type"), resultSet.getString("delivery_duration"),
////                resultSet.getString("no_of_revisions"), resultSet.getFloat("price"),
////                resultSet.getInt("no_of_concepts"), resultSet.getInt("package_id"));
//
//                PackagePricing newPackagePricing = new PackagePricing();
//                newPackagePricing.setPricePackageId(resultSet.getInt("price_package_id"));
//                newPackagePricing.setType(resultSet.getString("type"));
//                newPackagePricing.setDeliveryDuration(resultSet.getString("delivery_duration"));
//                newPackagePricing.setNoOfRevisions(resultSet.getString("no_of_revisions"));
//                newPackagePricing.setPrice(resultSet.getFloat("price"));
//                newPackagePricing.setNoOfConcepts(resultSet.getInt("no_of_concepts"));
//                newPackagePricing.setPackageId(resultSet.getInt("package_id"));
//
//
//                packagePricings.add(newPackagePricing);
//
//
//            }
//
//            return packagePricings;

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
