package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Package;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PackageService {

    public static int updatePackageData(Package newPackage){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "UPDATE package SET title=?, description=?, category=?, designer_userID=?, cover_url=?, clicks=?, orders=?, cancellations=?, status=? WHERE package_id=?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, newPackage.getTitle());
            preparedStatement.setString(2, newPackage.getDescription());
            preparedStatement.setString(3, newPackage.getCategory());
            preparedStatement.setInt(4,newPackage.getDesignerUserId());
            preparedStatement.setString(5, newPackage.getCoverUrl());
            preparedStatement.setInt(6, newPackage.getClicks());
            preparedStatement.setInt(7,newPackage.getOrders());
            preparedStatement.setString(8, newPackage.getCancellations());
            preparedStatement.setString(9, newPackage.getStatus());
            preparedStatement.setInt(10, newPackage.getPackageId());
            result = preparedStatement.executeUpdate();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        }
    }

    public static int insertPackageData(Package newPackage){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO package (package_id, title, description, category, designer_userID, cover_url, clicks, orders, cancellations, status) VALUES (?,?,?,?,?,?,?,?,?,?);";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, newPackage.getPackageId());
            preparedStatement.setString(2, newPackage.getTitle());
            preparedStatement.setString(3, newPackage.getDescription());
            preparedStatement.setString(4,newPackage.getCategory());
            preparedStatement.setInt(5, newPackage.getDesignerUserId());
            preparedStatement.setString(6, newPackage.getCoverUrl());
            preparedStatement.setInt(7, newPackage.getClicks());
            preparedStatement.setInt(8,newPackage.getOrders());
            preparedStatement.setString(9, newPackage.getCancellations());
            preparedStatement.setString(10, newPackage.getStatus());

            result = preparedStatement.executeUpdate();

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Package> getPackageData(int designerUserId) {

        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT package_id, title, description, category, cover_url, clicks, orders, cancellations, status, designer_userID FROM package WHERE designer_userID = ?;";
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
                Package newPackage = new Package(resultSet.getInt("package_id"),resultSet.getString("title"), resultSet.getString("description"), resultSet.getString("category"), resultSet.getString("cover_url"), resultSet.getInt("clicks"), resultSet.getInt("orders"), resultSet.getString("cancellations"), resultSet.getString("status"), resultSet.getInt("designer_userID"));
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
