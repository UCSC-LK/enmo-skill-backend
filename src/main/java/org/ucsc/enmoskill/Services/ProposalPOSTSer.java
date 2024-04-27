package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Pro_CR;
import org.ucsc.enmoskill.model.ProposalModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProposalPOSTSer {

    public boolean isInsertionSuccessful(ProposalModel proposal , Pro_CR proBRlist){

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {

            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO proposals (duration,price,title,pricingPackage,packageId,description ,requestID ,client_userID, designer_userID) VALUES (?, ?,?,?,?,?,?,?,?)";
            assert con != null;
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, proposal.getDeliveryDuration());
            preparedStatement.setString(2, proposal.getPrice());
            preparedStatement.setString(3, proposal.getTitle());
            preparedStatement.setString(4, proposal.getPricingPackage());
            preparedStatement.setString(5, proposal.getPackageId());
            preparedStatement.setString(6, proposal.getDescription());
            preparedStatement.setString(7, proBRlist.getRequestid());

            // Query to retrieve client_userID from request table based on requestID
            String clientUserIDQuery = "SELECT userID FROM jobs WHERE requestID = ?";
            PreparedStatement clientUserIDStatement = con.prepareStatement(clientUserIDQuery);
            clientUserIDStatement.setString(1, proBRlist.getRequestid());
            ResultSet clientUserIDResult = clientUserIDStatement.executeQuery();

            if (clientUserIDResult.next()) {
                String client_userID = clientUserIDResult.getString("userID");
                preparedStatement.setString(8, client_userID);
            } else {
                // Handle the case where no client_userID is found for the given requestID
            }

            preparedStatement.setString(9, proposal.getDesignerId());
            preparedStatement.executeUpdate(); // Execute the INSERT operation

            return true;
        }catch (SQLException e) {
            // Handle any exceptions that might occur during the insertion
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
