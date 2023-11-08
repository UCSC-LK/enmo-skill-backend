package org.ucsc.enmoskill.Services;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProposalDELETESer {

    private HttpServletResponse resp;
    public ProposalDELETESer(HttpServletResponse resp){
        this.resp = resp;
    }

    public void DeleteProposal(Connection connection, String proposalID, HttpServletResponse resp) {
        PreparedStatement preparedStatement = null;

        try {
            String query = "DELETE FROM proposals WHERE  proposalID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, proposalID); // Set the userID parameter
//            preparedStatement.setString(2, proposalID); // Set the proposalID parameter
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                resp.getWriter().write("Proposal deleted successfully.");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Proposal not found or deletion failed.");
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
