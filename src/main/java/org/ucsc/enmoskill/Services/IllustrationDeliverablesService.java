package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.IllustrationDeliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IllustrationDeliverablesService {

    public static int insertIDeliverables(IllustrationDeliverables newDeliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;


        try{
//        pricePackageID, Source file, High resolution, Background/scene, Color, Full body, Commercial use
            con = DatabaseConnection.initializeDatabase();

            String query = "INSERT INTO illustration_deliverables (price_package_id, source_file, high_resolution, background_scene, colour, full_body, commercial_use) VALUES(?,?,?,?,?,?,?)";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, newDeliverables.getPricePackageID());
            preparedStatement.setInt(2, newDeliverables.getSourceFile());
            preparedStatement.setInt(3, newDeliverables.getHighResolution());
            preparedStatement.setInt(4, newDeliverables.getBackground_Scene());
            preparedStatement.setInt(5, newDeliverables.getColour());
            preparedStatement.setInt(6, newDeliverables.getFullBody());
            preparedStatement.setInt(7, newDeliverables.getCommercialUse());

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

    public static IllustrationDeliverables getIllusDeliverables(int pricePackageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT price_package_id, source_file, high_resolution, background_scene, colour, full_body, commercial_use FROM illustration_deliverables WHERE price_package_id = ?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, pricePackageId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
                IllustrationDeliverables newDeliverables = new IllustrationDeliverables();

                while (resultSet.next()){
                    newDeliverables.setPricePackageID(resultSet.getInt("price_package_id"));
                    newDeliverables.setSourceFile(resultSet.getInt("source_file"));
                    newDeliverables.setHighResolution(resultSet.getInt("high_resolution"));
                    newDeliverables.setBackground_Scene(resultSet.getInt("background_scene"));
                    newDeliverables.setColour(resultSet.getInt("colour"));
                    newDeliverables.setFullBody(resultSet.getInt("full_body"));
                    newDeliverables.setCommercialUse(resultSet.getInt("commercial_use"));

                }

                return newDeliverables;
            }

            return null;
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

    public static int updateIllustDeliverables(IllustrationDeliverables newDeliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "UPDATE illustration_deliverables SET source_file=?, high_resolution=?, background_scene=?, colour=?, full_body=?, commercial_use=? WHERE price_package_id=?;";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, newDeliverables.getSourceFile());
            preparedStatement.setInt(2, newDeliverables.getHighResolution());
            preparedStatement.setInt(3, newDeliverables.getBackground_Scene());
            preparedStatement.setInt(4, newDeliverables.getColour());
            preparedStatement.setInt(5, newDeliverables.getFullBody());
            preparedStatement.setInt(6, newDeliverables.getCommercialUse());
            preparedStatement.setInt(7, newDeliverables.getPricePackageID());

            result = preparedStatement.executeUpdate();

            return result;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
