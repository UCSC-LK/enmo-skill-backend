package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.LogoDesignDeliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
