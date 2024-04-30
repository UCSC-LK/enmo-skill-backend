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

    public  void GetAllDesignersProposals(Connection connection , String userID ){
        PreparedStatement preparedStatement = null;
        ResultSet result = null; // Add this line to initialize the result set

        System.out.println("User ID: " + userID);
        try{
            String query = "SELECT \n" +
                    "    proposals.*, \n" +
                    "    jobs.discription, \n" +
                    "    package_pricing.price_package_id, \n" +
                    "    users.name AS client_name\n" +
                    "FROM \n" +
                    "    proposals \n" +
                    "JOIN \n" +
                    "    jobs ON proposals.requestID = jobs.requestID \n" +
                    "JOIN \n" +
                    "    package_pricing ON proposals.packageId = package_pricing.package_id \n" +
                    "                     AND proposals.pricingPackage = package_pricing.type\n" +
                    "JOIN \n" +
                    "    users ON proposals.client_userID = users.userID\n" +
                    "WHERE \n" +
                    "    proposals.designer_userID = ?;";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userID); // Set the userID parameter
            result = preparedStatement.executeQuery();
            System.out.println("Run: ");

            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()) {
                ProposalModel proposal = new ProposalModel(result);
                JsonObject jsonObject = gson.toJsonTree(proposal).getAsJsonObject();
                jsonObject.addProperty("client_name", result.getString("client_name"));
                jsonArray.add(jsonObject);
            }

            resp.getWriter().write(jsonArray.toString());
            resp.setStatus(HttpServletResponse.SC_OK);
            System.out.println("Proposals : " + jsonArray);

        }catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    public  void GetAllClientsProposals(Connection connection , String userID ){
        PreparedStatement preparedStatement = null;
        ResultSet result = null; // Add this line to initialize the result set

        System.out.println("User ID: " + userID);
        try{
            String query = "SELECT \n" +
                    "    proposals.*, \n" +
                    "    jobs.discription, \n" +
                    "    package_pricing.price_package_id, \n" +
                    "    users.name AS designer_name\n" +
                    "FROM \n" +
                    "    proposals \n" +
                    "JOIN \n" +
                    "    jobs ON proposals.requestID = jobs.requestID \n" +
                    "JOIN \n" +
                    "    package_pricing ON proposals.packageId = package_pricing.package_id \n" +
                    "                     AND proposals.pricingPackage = package_pricing.type\n" +
                    "JOIN \n" +
                    "    users ON proposals.designer_userID = users.userID\n" +
                    "WHERE \n" +
                    "    proposals.client_userID = ?;\n";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userID); // Set the userID parameter
            result = preparedStatement.executeQuery();
            System.out.println("Run: ");

            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()) {
                ProposalModel proposal = new ProposalModel(result);
                JsonObject jsonObject = gson.toJsonTree(proposal).getAsJsonObject();
                jsonObject.addProperty("designer_name", result.getString("designer_name"));
                jsonArray.add(jsonObject);
            }

            resp.getWriter().write(jsonArray.toString());
            resp.setStatus(HttpServletResponse.SC_OK);
            System.out.println("Proposals : " + jsonArray);

        }catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    public void GetProposal(Connection connection, String proposalID,String userID ,HttpServletResponse resp) {
        PreparedStatement preparedStatement = null;
        ResultSet result = null; // Add this line to initialize the result set

        try {
            String query = "SELECT \n" +
                    "    proposals.*, \n" +
                    "    jobs.discription, \n" +
                    "    package_pricing.price_package_id \n" +
                    "FROM \n" +
                    "    proposals \n" +
                    "JOIN \n" +
                    "    jobs ON proposals.requestID = jobs.requestID \n" +
                    "JOIN \n" +
                    "    package_pricing ON proposals.packageId = package_pricing.package_id AND proposals.pricingPackage = package_pricing.type \n" +
                    "WHERE \n" +
                    "    proposals.userID = ? \n" +
                    "    AND proposals.proposalID = ?\n";
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



