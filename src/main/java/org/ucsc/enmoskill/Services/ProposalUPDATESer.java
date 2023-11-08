package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.model.ProposalModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProposalUPDATESer {

    private HttpServletResponse resp;

    public ProposalUPDATESer(HttpServletResponse resp){
        this.resp = resp;
    }

    public void UpdateProposal(Connection connection, String proposalID,ProposalModel proposalModel, HttpServletResponse resp) {
        PreparedStatement preparedStatement = null;

        try {
            String query = "UPDATE proposals SET description = ?, date = ?, budget = ?, duration = ? WHERE proposalID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, proposalModel.getDescription()); // Set the new description
            preparedStatement.setString(2, proposalModel.getDate()); // Set the new date
            preparedStatement.setString(3, proposalModel.getBudget()); // Set the new budget
            preparedStatement.setString(4, proposalModel.getDuration()); // Set the new duration
            preparedStatement.setString(5, proposalID); // Set the proposalID parameter

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                resp.getWriter().write("Proposal updated successfully.");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Proposal not found or update failed.");
            }
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        } finally {
            // Close the prepared statement and connection (if needed)
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                // You may also want to close the connection if it's not managed elsewhere
                // connection.close();
            } catch (SQLException e) {
                // Handle or log any SQLException that occurs during closing
                e.printStackTrace();
            }
        }
    }

}
