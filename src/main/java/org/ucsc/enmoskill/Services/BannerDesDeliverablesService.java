package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BannerDesignDeliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BannerDesDeliverablesService {

    public static int insertBDDeliverables(BannerDesignDeliverables newDeliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO banner_design_deliverables (price_package_id, custom_graphics, source_file, print_ready) VALUES(?,?,?,?);";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, newDeliverables.getPricePackageID());
            preparedStatement.setInt(2, newDeliverables.getPrintReady());
            preparedStatement.setInt(3, newDeliverables.getSourceFile());
            preparedStatement.setInt(4, newDeliverables.getCustomGraphics());

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

    public static BannerDesignDeliverables getBDDeliverables(int pricePackageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT price_package_id, custom_graphics, source_file, print_ready FROM banner_design_deliverables WHERE price_package_id = ?;";

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, pricePackageId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
                BannerDesignDeliverables newDeliverables = new BannerDesignDeliverables();

                while (resultSet.next()){
                    newDeliverables.setPricePackageID(resultSet.getInt("price_package_id"));
                    newDeliverables.setCustomGraphics(resultSet.getInt("custom_graphics"));
                    newDeliverables.setSourceFile(resultSet.getInt("source_file"));
                    newDeliverables.setPrintReady(resultSet.getInt("print_ready"));

                }

                return  newDeliverables;
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

    public static int updateBDDeliverables(BannerDesignDeliverables newDeliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

//        custom_graphics, source_file, print_ready
        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "UPDATE banner_design_deliverables SET custom_graphics=?, source_file=?, print_ready=? WHERE price_package_id=?;";

            preparedStatement = con.prepareStatement(query);

            preparedStatement.setInt(1, newDeliverables.getCustomGraphics());
            preparedStatement.setInt(2, newDeliverables.getSourceFile());
            preparedStatement.setInt(3, newDeliverables.getPrintReady());
            preparedStatement.setInt(4, newDeliverables.getPricePackageID());

            result = preparedStatement.executeUpdate();

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
