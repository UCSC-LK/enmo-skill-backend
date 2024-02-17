package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.PackageDeliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PackageDeliverablesService {

    String query;

    public int insertPackageDeliverables(PackageDeliverables deliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        // 14
        try{
            con = DatabaseConnection.initializeDatabase();
             query = "INSERT INTO package_deliverables(price_package_id, deliverables_count, transparent_file, vector_file, printable_file, mockup, source_file, social_media_kit, high_resolution, background_scene, colour, full_body, commercial_use, double_sided, custom_graphics, photo_editing) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
             preparedStatement = con.prepareStatement(query);
             preparedStatement.setInt(1, deliverables.getPricePackageId());
            preparedStatement.setInt(2, deliverables.getDeliverablesCount());
            preparedStatement.setInt(3, deliverables.getTransparentFile());
            preparedStatement.setInt(4, deliverables.getVectorFile());
            preparedStatement.setInt(5, deliverables.getPrintableFile());
            preparedStatement.setInt(6, deliverables.getMockup());
            preparedStatement.setInt(7, deliverables.getSourceFile());
            preparedStatement.setInt(8, deliverables.getSocialMediaKit());
            preparedStatement.setInt(9, deliverables.getHighResolution());
            preparedStatement.setInt(10, deliverables.getBackground_scene());
            preparedStatement.setInt(11, deliverables.getColour());
            preparedStatement.setInt(12, deliverables.getFullBody());
            preparedStatement.setInt(13, deliverables.getCommercialUse());
            preparedStatement.setInt(14, deliverables.getDoubleSided());
            preparedStatement.setInt(15, deliverables.getCustomGraphics());
            preparedStatement.setInt(16, deliverables.getPhotoEditing());

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

    public PackageDeliverables getPackageDeliverables(int pricePackageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            query = "SELECT * FROM package_deliverables WHERE price_package_id=?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,pricePackageId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
                PackageDeliverables newDeliverables = new PackageDeliverables();

                while (resultSet.next()) {
                    newDeliverables.setPricePackageId(resultSet.getInt("price_package_id"));
                    newDeliverables.setDeliverablesCount(resultSet.getInt("deliverables_count"));
                    newDeliverables.setTransparentFile(resultSet.getInt("transparent_file"));
                    newDeliverables.setVectorFile(resultSet.getInt("vector_file"));
                    newDeliverables.setPrintableFile(resultSet.getInt("printable_file"));
                    newDeliverables.setMockup(resultSet.getInt("mockup"));
                    newDeliverables.setSourceFile(resultSet.getInt("source_file"));
                    newDeliverables.setSocialMediaKit(resultSet.getInt("social_media_kit"));
                    newDeliverables.setHighResolution(resultSet.getInt("high_resolution"));
                    newDeliverables.setBackground_scene(resultSet.getInt("background_scene"));
                    newDeliverables.setColour(resultSet.getInt("colour"));
                    newDeliverables.setFullBody(resultSet.getInt("full_body"));
                    newDeliverables.setCommercialUse(resultSet.getInt("commercial_use"));
                    newDeliverables.setDoubleSided(resultSet.getInt("double_sided"));
                    newDeliverables.setCustomGraphics(resultSet.getInt("custom_graphics"));
                    newDeliverables.setPhotoEditing(resultSet.getInt("photo_editing"));
                }
                return newDeliverables;
            } else
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

    public int updatePackageDeliverables(PackageDeliverables deliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "UPDATE package_deliverables SET " +
                    "price_package_id=?, " +
                    "deliverables_count=?, " +
                    "transparent_file=?, " +
                    "vector_file=?, " +
                    "printable_file=?, " +
                    "mockup=?, " +
                    "source_file=?, " +
                    "social_media_kit=?, " +
                    "high_resolution=?, " +
                    "background_scene=?, " +
                    "colour=?, " +
                    "full_body=?, " +
                    "commercial_use=?, " +
                    "double_sided=?, " +
                    "custom_graphics=?, " +
                    "photo_editing=? " +
                    "WHERE deliverables_id=?";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, deliverables.getPricePackageId());
            preparedStatement.setInt(2, deliverables.getDeliverablesCount());
            preparedStatement.setInt(3, deliverables.getTransparentFile());
            preparedStatement.setInt(4, deliverables.getVectorFile());
            preparedStatement.setInt(5, deliverables.getPrintableFile());
            preparedStatement.setInt(6, deliverables.getMockup());
            preparedStatement.setInt(7, deliverables.getSourceFile());
            preparedStatement.setInt(8, deliverables.getSocialMediaKit());
            preparedStatement.setInt(9, deliverables.getHighResolution());
            preparedStatement.setInt(10, deliverables.getBackground_scene());
            preparedStatement.setInt(11, deliverables.getColour());
            preparedStatement.setInt(12, deliverables.getFullBody());
            preparedStatement.setInt(13, deliverables.getCommercialUse());
            preparedStatement.setInt(14, deliverables.getDoubleSided());
            preparedStatement.setInt(15, deliverables.getCustomGraphics());
            preparedStatement.setInt(16, deliverables.getPhotoEditing());
            preparedStatement.setInt(17, deliverables.getDeliverablesId());

            result = preparedStatement.executeUpdate();

            return result;

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
}
