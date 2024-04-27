package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.ProposalDELETESer;
import org.ucsc.enmoskill.Services.ProposalGETSer;
import org.ucsc.enmoskill.Services.ProposalPOSTSer;
import org.ucsc.enmoskill.Services.ProposalUPDATESer;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Pro_CR;
import org.ucsc.enmoskill.model.ProposalModel;
import org.ucsc.enmoskill.utils.AuthorizationResults;
import org.ucsc.enmoskill.utils.AuthorizationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;


import static java.lang.System.out;

public class ProposalController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        Connection connection = DatabaseConnection.initializeDatabase();
        Pro_CR proBRlist = new Pro_CR(req);

        out.println("ProposalID: " + proBRlist.getProposalid());
        out.println("UserID: " + proBRlist.getUserid());
        out.println("Role: " + proBRlist.getRole());
        out.println("req: " + req);

        String jwtToken = req.getHeader("Authorization");
        out.println("TOken" + jwtToken);

        if (jwtToken != null) {

            AuthorizationService authService = new AuthorizationService();
            String[] expectedUserLevelIDs = {"1", "2"}; // Set your expected userLevelIDs here

            AuthorizationResults authResult = authService.authorize(jwtToken, expectedUserLevelIDs);

            if (authResult != null) {
                String userID = authResult.getUserID();
                String userLevelID = authResult.getJwtUserLevelID();

                out.println("authresultID: " + userID);
                out.println("AuthresultlevelID: " + userLevelID);

                if (userLevelID != null && userID != null) {
                    if("2".equals(userLevelID)) {
                        if (proBRlist.getProposalid() == null) {
                            ProposalGETSer service = new ProposalGETSer(resp);
                            service.GetAllDesignersProposals(connection, userID);
                        } else {
                            ProposalGETSer service = new ProposalGETSer(resp);
                            service.GetProposal(connection, proBRlist.getProposalid(), userID, resp);
                        }
                    } else if ("1".equals(userLevelID)) {
                        if (proBRlist.getProposalid() == null) {
                            ProposalGETSer service = new ProposalGETSer(resp);
                            service.GetAllClientsProposals(connection, userID);
                        } else {
                            ProposalGETSer service = new ProposalGETSer(resp);
                            service.GetProposal(connection, proBRlist.getProposalid(), userID, resp);
                        }
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("User ID or User Level ID is null!");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("User is not authorized to perform this action!");
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("No JWT token found in the request header");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            // Create a Gson instance
            Gson gson = new Gson();

            // Read JSON data from the request body
            BufferedReader reader = req.getReader();
            ProposalModel proposal = gson.fromJson(reader, ProposalModel.class);

            Pro_CR proBRlist = new Pro_CR(req);

            out.println("Duration" + proposal.getDeliveryDuration());
            out.println("getDescription: " + proposal.getDescription());
            out.println("getPrice: " + proposal.getPrice());
            out.println("getTitle: " + proposal.getTitle());
            out.println("getPricingPackage: " + proposal.getPricingPackage());
            out.println("getPackageId: " + proposal.getPackageId());

            // Get the JWT token from the request header
            String jwtToken = req.getHeader("Authorization");

            // Check if the JWT token is present
            if (jwtToken != null) {

                // Use the AuthorizationService to authorize the request
                AuthorizationService authService = new AuthorizationService();
                String[] expectedUserLevelID = {"2"}; // Set your expected userLevelID here

                AuthorizationResults authResult = authService.authorize(jwtToken, expectedUserLevelID);

                if (authResult != null) {
                    String designerID = authResult.getUserID();
                    String userLevelID = authResult.getJwtUserLevelID();

                    if (designerID != null && userLevelID != null && proposal.getBudget() != null
                            && proposal.getDescription() != null && proposal.getDuration() != null) {

                        proposal.setDesignerId(designerID);
                        ProposalPOSTSer proposalPOSTSer = new ProposalPOSTSer();
                        boolean isSuccess = proposalPOSTSer.isInsertionSuccessful(proposal, proBRlist);

                        if (isSuccess) {
                            resp.setStatus(HttpServletResponse.SC_OK);
                            resp.getWriter().write("Proposal submitted successfully");
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().write("Proposal submitted unsuccessfully");
                        }
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("Required Field Missing");
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("User is not authorized to perform this action!");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println("No JWT token found in the request header");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Print the exception details for debugging
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }



    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Connection connection = DatabaseConnection.initializeDatabase();
        Pro_CR proBRlist = new Pro_CR(req);

        out.println("ProposalID: " + proBRlist.getProposalid());
        out.println("UserID: " + proBRlist.getUserid());
        out.println("Role: " + proBRlist.getRole());

        String jwtToken = req.getHeader("Authorization");

        // Check if the JWT token is present
        if (jwtToken != null) {

            // Use the AuthorizationService to authorize the request
            AuthorizationService authService = new AuthorizationService();
            String[] expectedUserLevelID = {"2"}; // Set your expected userLevelID here

            AuthorizationResults authResult = authService.authorize(jwtToken, expectedUserLevelID);

            out.println("authResult -- : " + authResult);

            if (authResult != null) {
                String userID = authResult.getUserID();
                String userLevelID = authResult.getJwtUserLevelID();

                out.println("UserID: " + userID);
                out.println("userLevelID: " + userLevelID);


                try {
                    if (proBRlist.getProposalid() == null) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("Proposal ID is Required!");
                    } else {
                        ProposalDELETESer service = new ProposalDELETESer(resp);
                        service.DeleteProposal(connection, proBRlist.getProposalid(), resp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("Error processing the request");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("User is not authorized to perform this action!");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("No JWT token found in the request header");
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Connection connection = DatabaseConnection.initializeDatabase();
        Pro_CR proBRlist = new Pro_CR(req);

        // Create a Gson instance
        Gson gson = new Gson();

        // Read JSON data from the request body
        BufferedReader reader = req.getReader();
        ProposalModel proposal = gson.fromJson(reader, ProposalModel.class);

        out.println("ProposalID: " + proBRlist.getProposalid());
        out.println("UserID: " + proBRlist.getUserid());
        out.println("Role: " + proBRlist.getRole());

        String jwtToken = req.getHeader("Authorization");

        // Check if the JWT token is present
        if (jwtToken != null) {

            // Use the AuthorizationService to authorize the request
            AuthorizationService authService = new AuthorizationService();
            String[] expectedUserLevelID = {"2"}; // Set your expected userLevelID here

            AuthorizationResults authResult = authService.authorize(jwtToken, expectedUserLevelID);

            if (authResult != null) {
                String userID = authResult.getUserID();
                String userLevelID = authResult.getJwtUserLevelID();

                try {
                    if (proBRlist.getProposalid() == null) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("Proposal ID is Required!");
                    } else {
                        ProposalUPDATESer service = new ProposalUPDATESer(resp);
                        service.UpdateProposal(connection, proBRlist.getProposalid(), proposal, resp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("Error processing the request");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("User is not authorized to perform this action!");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("No JWT token found in the request header");
        }
    }
}
