package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.FlyerDesignDeliverables;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FlyerDesDeliverablesService {

    public static int insertFDDeliverables(FlyerDesignDeliverables newDeliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO flyer_design_deliverables (price_package_id, print_ready, source_file, double_sided, custom_graphics, photo_editing, social_media_design, commercial_use) VALUES(?,?,?,?,?,?,?,?);";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, newDeliverables.getPricePackageID());
            preparedStatement.setInt(2, newDeliverables.getPrintReady());
            preparedStatement.setInt(3, newDeliverables.getSourceFile());
            preparedStatement.setInt(4, newDeliverables.getDoubleSided());
            preparedStatement.setInt(5, newDeliverables.getCustomGraphics());
            preparedStatement.setInt(6, newDeliverables.getPhotoEditing());
            preparedStatement.setInt(7, newDeliverables.getSocialMediaDesign());
            preparedStatement.setInt(8, newDeliverables.getCommercialUse());

            result = preparedStatement.executeUpdate();

            return result;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the database connections in a finally block
            try {
//                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exceptions during closing connections if needed
            }
        }
    }

    public static FlyerDesignDeliverables getFDDeliverables(int pricePackageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT price_package_id, print_ready, source_file, double_sided, custom_graphics, photo_editing, social_media_design, commercial_use FROM flyer_design_deliverables WHERE price_package_id = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, pricePackageId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
                FlyerDesignDeliverables newDeliverables = new FlyerDesignDeliverables();

                while (resultSet.next()){
                    newDeliverables.setPricePackageID(resultSet.getInt("price_package_id"));
                    newDeliverables.setPrintReady(resultSet.getInt("print_ready"));
                    newDeliverables.setSourceFile(resultSet.getInt("source_file"));
                    newDeliverables.setDoubleSided(resultSet.getInt("double_sided"));
                    newDeliverables.setCustomGraphics(resultSet.getInt("custom_graphics"));
                    newDeliverables.setPhotoEditing(resultSet.getInt("photo_editing"));
                    newDeliverables.setSocialMediaDesign(resultSet.getInt("social_media_design"));
                    newDeliverables.setCommercialUse(resultSet.getInt("commercial_use"));

                }
                return newDeliverables;
            }
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

    public static int updateFDDeliverables(FlyerDesignDeliverables newDeliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;


        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "UPDATE flyer_design_deliverables SET print_ready=?, source_file=?, double_sided=?, custom_graphics=?, photo_editing=?, social_media_design=?, commercial_use=? WHERE price_package_id=?;";

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, newDeliverables.getPrintReady());
            preparedStatement.setInt(2, newDeliverables.getSourceFile());
            preparedStatement.setInt(3, newDeliverables.getDoubleSided());
            preparedStatement.setInt(4, newDeliverables.getCustomGraphics());
            preparedStatement.setInt(5, newDeliverables.getPhotoEditing());
            preparedStatement.setInt(6, newDeliverables.getSocialMediaDesign());
            preparedStatement.setInt(7, newDeliverables.getCommercialUse());
            preparedStatement.setInt(8, newDeliverables.getPricePackageID());

            result = preparedStatement.executeUpdate();

            return result;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
