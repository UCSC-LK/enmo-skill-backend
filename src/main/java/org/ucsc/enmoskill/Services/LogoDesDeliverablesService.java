package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.LogoDesignDeliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogoDesDeliverablesService {

    public static int insertLDDeliverables(LogoDesignDeliverables newDeliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

//        pricePackageID, Logo transparency, Vector file,, Printable file, 3D mockup, Source file, Social media kit)

        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "INSERT INTO logo_design_deliverables (price_package_id, logo_transparency, vector_file, printable_file, 3d_mockup, source_file, social_media_kit) values(?,?,?,?,?,?,?);";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,newDeliverables.getPricePackageId());
            preparedStatement.setInt(2,newDeliverables.getLogoTransparency());
            preparedStatement.setInt(3,newDeliverables.getVectorFile());
            preparedStatement.setInt(4,newDeliverables.getPrintableFile());
            preparedStatement.setInt(5,newDeliverables.getMockup());
            preparedStatement.setInt(6,newDeliverables.getSourceFile());
            preparedStatement.setInt(7,newDeliverables.getSocialMediaKit());

            result = preparedStatement.executeUpdate();

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static LogoDesignDeliverables getLDDeliverables(int pricePackageId){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "SELECT price_package_id, logo_transparency, vector_file, printable_file, 3d_mockup, source_file, social_media_kit FROM logo_design_deliverables WHERE price_package_id = ?;";

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, pricePackageId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet != null){
                LogoDesignDeliverables newDeliverables = new LogoDesignDeliverables();

                while (resultSet.next()){
                    newDeliverables.setPricePackageId(resultSet.getInt("price_package_id"));
                    newDeliverables.setLogoTransparency(resultSet.getInt("logo_transparency"));
                    newDeliverables.setVectorFile(resultSet.getInt("vector_file"));
                    newDeliverables.setPrintableFile(resultSet.getInt("printable_file"));
                    newDeliverables.setMockup(resultSet.getInt("3d_mockup"));
                    newDeliverables.setSourceFile(resultSet.getInt("source_file"));
                    newDeliverables.setSocialMediaKit(resultSet.getInt("social_media_kit"));
                }

                return  newDeliverables;

            } else
                return null;







        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int updateLDDeliverables(LogoDesignDeliverables deliverables){
        Connection con = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        try{
            con = DatabaseConnection.initializeDatabase();

            String query = "UPDATE logo_design_deliverables SET logo_transparency=?, vector_file=?, printable_file=?, 3d_mockup=?, source_file=?, social_media_kit=? WHERE price_package_id=?;";

            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, deliverables.getLogoTransparency());
            preparedStatement.setInt(2, deliverables.getVectorFile());
            preparedStatement.setInt(3, deliverables.getPrintableFile());
            preparedStatement.setInt(4,deliverables.getMockup());
            preparedStatement.setInt(5, deliverables.getSourceFile());
            preparedStatement.setInt(6, deliverables.getSocialMediaKit());
            preparedStatement.setInt(7, deliverables.getPricePackageId());

            result = preparedStatement.executeUpdate();

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
