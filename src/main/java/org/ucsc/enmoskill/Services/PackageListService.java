package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Package;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PackageListService {

//    public static List<Package> getFilteredPackages(int category, int priceCode, int delTimeCode, int language){
//        Connection con = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//        String query = null;
//        List<Package> packageList = null;
//
//        try {
//            con = DatabaseConnection.initializeDatabase();
//
//            if (priceCode == 0 && delTimeCode == 0 && language == 0){
//                packageList = getAllPackages(category);
//            } else if (priceCode == 1 && delTimeCode == 0 && language == 0) {
//                System.out.println("hi");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return packageList;
//
//    }

    public static List<Package> getHighPackages(){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            query = "SELECT * FROM package WHERE package_id IN (SELECT package_id FROM package_pricing WHERE (type = 'bronze' AND price >= 5000))";

            preparedStatement = con.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getMidPackages(){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;



        try{
            con = DatabaseConnection.initializeDatabase();

            query = "SELECT * FROM package WHERE package_id IN (SELECT package_id FROM package_pricing WHERE (type = 'bronze' AND price between 2000 AND 5000))";

            preparedStatement = con.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);
            return packages;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Package> geLowPackages(){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            query = "SELECT * FROM package WHERE package_id IN (SELECT package_id FROM package_pricing WHERE type='platinum' AND price<=2000)";

            preparedStatement = con.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getAllPackages(int category){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = null;

        try{
            con = DatabaseConnection.initializeDatabase();

            if (category == 0){
                query = "SELECT package_id, title, description, category, cover_url, clicks, orders, cancellations, status, designer_userID, insertion_time FROM package WHERE status='active' ORDER BY clicks desc;";
                preparedStatement = con.prepareStatement(query);

            } else{
                query = "SELECT package_id, title, description, category, cover_url, clicks, orders, cancellations, status, designer_userID, insertion_time FROM package WHERE status='active' AND category=? ORDER BY clicks desc;";
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, category);
            }


            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

            packages = unwrap(resultSet);

            return packages;

        } catch (Exception e) {
            // Handle exceptions here
            e.printStackTrace();
            // You might want to throw an exception or return null here, depending on your requirements.
            return null;
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

    public static List<Package> unwrap(ResultSet resultSet){
        List<Package> packages = new ArrayList<>();

        try {
            while (resultSet.next()){
                Package newPackage = new Package(resultSet.getInt("package_id"),resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("category"), resultSet.getString("cover_url"), resultSet.getInt("clicks"), resultSet.getInt("orders"), resultSet.getString("cancellations"), resultSet.getString("status"), resultSet.getInt("designer_userID"), resultSet.getTimestamp("insertion_time"));
                packages.add(newPackage);
            }
            return packages;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
