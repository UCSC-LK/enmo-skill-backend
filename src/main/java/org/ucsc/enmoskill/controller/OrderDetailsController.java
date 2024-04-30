package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.OrderDetailsService;
import org.ucsc.enmoskill.Services.ProposalGETSer;
import org.ucsc.enmoskill.Services.ProposalPOSTSer;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.OrderDetailsModel;
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
        PrintWriter out = resp.getWriter();

        try {
            // Create a Gson instance
            Gson gson = new Gson();

            // Read JSON data from the request body
            BufferedReader reader = req.getReader();
            OrderDetailsModel orderDetails = gson.fromJson(reader, OrderDetailsModel.class);


            out.println("Duration" + orderDetails.getOrderID());
            out.println("getDescription: " + orderDetails.getOrder_details_id());
            out.println("getPrice: " + orderDetails.getClientId());
            out.println("getTitle: " + orderDetails.getClient_message());
            out.println("getPricingPackage: " + orderDetails.getDesigner_message());


            // Get the JWT token from the request header
            String jwtToken = req.getHeader("Authorization");

            // Check if the JWT token is present
            if (jwtToken != null) {

                // Use the AuthorizationService to authorize the request
                AuthorizationService authService = new AuthorizationService();
                String[] expectedUserLevelID = {"1", "2"}; // Set your expected userLevelID here

                AuthorizationResults authResult = authService.authorize(jwtToken, expectedUserLevelID);

                if (authResult != null) {
                    String userID = authResult.getUserID();
                    String userLevelID = authResult.getJwtUserLevelID();

                    out.println("authresultID: " + userID);
                    out.println("AuthresultlevelID: " + userLevelID);

                    if (userLevelID != null && userID != null) {
                        if ("2".equals(userLevelID)) {
                            int designerId = Integer.parseInt(userID);
                            orderDetails.setDesignerId(designerId);
                            OrderDetailsService service = new OrderDetailsService(resp);
                            boolean isSuccess = service.isDesignerOrderDetailsInsertionSuccessful(orderDetails);

                            if (isSuccess) {
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resp.getWriter().write("Order Details insert successfully");
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                resp.getWriter().write("Order Details insert unsuccessfully");
                            }
                        } else if ("1".equals(userLevelID)) {
                            int clientID = Integer.parseInt(userID);
                            orderDetails.setClientId(clientID);
                            OrderDetailsService service = new OrderDetailsService(resp);
                            boolean isSuccess = service.isClientOrderDetailsInsertionSuccessful(orderDetails);

                            if (isSuccess) {
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resp.getWriter().write("Order Details insert successfully");
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                resp.getWriter().write("Order Details insert unsuccessfully");
                            }
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

        } catch (Exception e) {
            e.printStackTrace(); // Print the exception details for debugging
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }
}
