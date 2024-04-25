package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.OrderDetailsService;
import org.ucsc.enmoskill.Services.ProposalGETSer;
import org.ucsc.enmoskill.Services.ProposalPOSTSer;
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
import java.io.PrintWriter;
import java.sql.Connection;

import static java.lang.System.out;

public class OrderDetailsController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        Connection connection = DatabaseConnection.initializeDatabase();


        // Get the order ID from the request parameter
        String orderIdParam = req.getParameter("orderID");
        int OrderID;
        if (orderIdParam != null) {
            try {
                OrderID = Integer.parseInt(orderIdParam);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Invalid order ID format");
                return; // Stop further processing
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Order ID not specified");
            return; // Stop further processing
        }

        String jwtToken = req.getHeader("Authorization");
//        out.println("TOKen " + jwtToken);

        if (jwtToken != null) {

            AuthorizationService authService = new AuthorizationService();
            String[] expectedUserLevelIDs = {"1", "2"}; // Set your expected userLevelIDs here

            AuthorizationResults authResult = authService.authorize(jwtToken, expectedUserLevelIDs);

            if (authResult != null) {
                String userID = authResult.getUserID();
                String userLevelID = authResult.getJwtUserLevelID();

//                out.println("authresultID: " + userID);
//                out.println("AuthresultlevelID: " + userLevelID);

                if (userLevelID != null && userID != null) {
                    if ("2".equals(userLevelID)) {
                        OrderDetailsService service = new OrderDetailsService(resp);
                        service.GetOrderDetails(connection, OrderID);
                    } else if ("1".equals(userLevelID)) {
                        OrderDetailsService service = new OrderDetailsService(resp);
                        service.GetOrderDetails(connection, OrderID);
                     }else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("ERROR");
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
}
