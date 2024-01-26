package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Package;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PackageListService {

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

            while (resultSet.next()){
                Package newPackage = new Package(resultSet.getInt("package_id"),resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("category"), resultSet.getString("cover_url"), resultSet.getInt("clicks"), resultSet.getInt("orders"), resultSet.getString("cancellations"), resultSet.getString("status"), resultSet.getInt("designer_userID"), resultSet.getTimestamp("insertion_time"));
                packages.add(newPackage);
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
