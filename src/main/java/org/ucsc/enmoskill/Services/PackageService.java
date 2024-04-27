package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageService {

    public static int updatePackageData(Package newPackage){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "UPDATE package SET title=?, description=?, category=?, designer_userID=?, cover_url=?, status=? WHERE package_id=?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, newPackage.getTitle());
            preparedStatement.setString(2, newPackage.getDescription());
            preparedStatement.setInt(3, newPackage.getCategory());
            preparedStatement.setInt(4,newPackage.getDesignerUserId());
            preparedStatement.setString(5, newPackage.getCoverUrl());
            preparedStatement.setString(6, newPackage.getStatus());
            preparedStatement.setInt(7, newPackage.getPackageId());
            result = preparedStatement.executeUpdate();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static int deletePackageData(Package newPackage){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;


        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "DELETE FROM package WHERE package_id=?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, newPackage.getPackageId());
            result = preparedStatement.executeUpdate();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static int insertPackageData(Package newPackage){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO package (title, description, category, designer_userID, cover_url, clicks, orders, cancellations, status) VALUES (?,?,?,?,?,?,?,?,?);";
            preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//            preparedStatement.setInt(1, newPackage.getPackageId());
            preparedStatement.setString(1, newPackage.getTitle());
            preparedStatement.setString(2, newPackage.getDescription());
            preparedStatement.setInt(3,newPackage.getCategory());
            preparedStatement.setInt(4, newPackage.getDesignerUserId());
            preparedStatement.setString(5, newPackage.getCoverUrl());
            preparedStatement.setInt(6, newPackage.getClicks());
            preparedStatement.setInt(7,newPackage.getOrders());
            preparedStatement.setString(8, newPackage.getCancellations());
            preparedStatement.setString(9, newPackage.getStatus());

            result = preparedStatement.executeUpdate();

            if (result > 0) {
                // Retrieve the auto-generated keys (including the primary key)
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);

                    } else {
                        throw new SQLException("Creating package failed, no ID obtained.");
                    }
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Package getPackage(int packageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT package_id, title, description, category, cover_url, clicks, orders, cancellations, status, designer_userID, insertion_time FROM package WHERE package_id = ?;";

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, packageId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
                Package newPackage = new Package();

                while (resultSet.next()){
                    newPackage.setPackageId(resultSet.getInt("package_id"));
                    newPackage.setDesignerUserId(resultSet.getInt("designer_userID"));
                    newPackage.setStatus(resultSet.getString("status"));
                    newPackage.setCategory(resultSet.getInt("category"));
                    newPackage.setClicks(resultSet.getInt("clicks"));
                    newPackage.setCancellations(resultSet.getString("cancellations"));
                    newPackage.setCoverUrl(resultSet.getString("cover_url"));
                    newPackage.setDescription(resultSet.getString("description"));
                    newPackage.setOrders(resultSet.getInt("orders"));
                    newPackage.setTitle(resultSet.getString("title"));
                    newPackage.setInsertionTime(resultSet.getTimestamp("insertion_time"));

                }

                return newPackage;
            } else
                return null;





        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
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

    public static List<Package> getPackageData(int designerUserId) {

        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT package_id, title, description, category, cover_url, clicks, orders, cancellations, status, designer_userID, insertion_time FROM package WHERE designer_userID = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, designerUserId);

            resultSet = preparedStatement.executeQuery();

            List<Package> packages = new ArrayList<>();

//            System.out.println(resultSet.getString("title"));

            // next() - go to the text line in the result set
//            if (resultSet.next()) {
//                System.out.println();
//                return new Package(resultSet.getString("title"), resultSet.getString("description"), resultSet.getString("category"), resultSet.getInt("designer_userID"));
//            } else {
//                // If no records are found, return an empty Package object
//                return new Package("", "", "", 0);
//            }

//            if (resultSet.next()){
////                Package newPackage = new Package("", "", "", 0);
//                // If no records are found, return an empty Package object
////                return new Package("", "", "", 0);
////                packages.add(newPackage);
////                return packages;
//
//
//            }

            while (resultSet.next()){
                Package newPackage = new Package(resultSet.getInt("package_id"),resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("category"), resultSet.getString("cover_url"), resultSet.getInt("clicks"), resultSet.getInt("orders"), resultSet.getString("cancellations"), resultSet.getString("status"), resultSet.getInt("designer_userID"), resultSet.getTimestamp("insertion_time"));
                packages.add(newPackage);
//
            }


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
}