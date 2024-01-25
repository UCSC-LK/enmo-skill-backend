package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Pro_CR;
import org.ucsc.enmoskill.model.ProposalModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProposalPOSTSer {

    public boolean isInsertionSuccessful(ProposalModel proposal , Pro_CR proBRlist ,String userID){

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO proposals (duration,budget, userID,description ,requestID) VALUES (?, ?,?,? ,?)";
            assert con != null;
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, proposal.getDuration());
            preparedStatement.setString(2, proposal.getBudget());
            preparedStatement.setString(3, userID);
//            preparedStatement.setString(4, proposal.getDate());
            preparedStatement.setString(4, proposal.getDescription());
            preparedStatement.setString(5, proBRlist.getRequestid());
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
