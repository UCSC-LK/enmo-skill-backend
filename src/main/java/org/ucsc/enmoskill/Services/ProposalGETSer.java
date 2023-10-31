package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.model.ProposalModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProposalGETSer {
    private HttpServletResponse resp;
    public ProposalGETSer(HttpServletResponse resp){
        this.resp = resp;
    }

    public  void GetAllProposals(Connection connection , String userID ){
        PreparedStatement preparedStatement = null;
        ResultSet result = null; // Add this line to initialize the result set

        try{
            String query = "SELECT * FROM proposals WHERE userID = ? ";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userID); // Set the userID parameter
            result = preparedStatement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()) {
                ProposalModel proposal = new ProposalModel(result);
                JsonObject jsonObject = gson.toJsonTree(proposal).getAsJsonObject();
                jsonArray.add(jsonObject);
            }

            resp.getWriter().write(jsonArray.toString());
            resp.setStatus(HttpServletResponse.SC_OK);

        }catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    public void GetProposal(Connection connection, String proposalID,String userID ,HttpServletResponse resp) {
        PreparedStatement preparedStatement = null;
        ResultSet result = null; // Add this line to initialize the result set

        try {
            String query = "SELECT * FROM proposals WHERE userID = ? AND proposalID = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userID); // Set the userID parameter
            preparedStatement.setString(2, proposalID); // Set the proposalID parameter
            result = preparedStatement.executeQuery();


            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()) {
                ProposalModel proposal = new ProposalModel(result);
                JsonObject jsonObject = gson.toJsonTree(proposal).getAsJsonObject();
                jsonArray.add(jsonObject);
            }

            resp.getWriter().write(jsonArray.toString());
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        } finally {
            // Close the result set, prepared statement, and connection (if needed)
            try {
                if (result != null) {
                    result.close();
                }
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



