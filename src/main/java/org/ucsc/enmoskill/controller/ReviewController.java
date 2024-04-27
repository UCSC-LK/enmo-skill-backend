package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.ReviewService;
import org.ucsc.enmoskill.model.ProposalModel;
import org.ucsc.enmoskill.model.ReviewModel;
import org.ucsc.enmoskill.utils.AuthorizationResults;
import org.ucsc.enmoskill.utils.AuthorizationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static java.lang.System.out;

public class ReviewController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {

            // Create a Gson instance
            Gson gson = new Gson();

            // Read JSON data from the request body
            BufferedReader reader = req.getReader();
            ReviewModel review = gson.fromJson(reader, ReviewModel.class);

            // Get the JWT token from the request header
            String jwtToken = req.getHeader("Authorization");

            // Check if the JWT token is present
            if (jwtToken != null) {

                // Use the AuthorizationService to authorize the request
                AuthorizationService authService = new AuthorizationService();
                String[] expectedUserLevelID = {"1"}; // Set your expected userLevelID here

                AuthorizationResults authResult = authService.authorize(jwtToken, expectedUserLevelID);

                if (authResult != null) {
                    String clientID = authResult.getUserID();
                    String userLevelID = authResult.getJwtUserLevelID();
                    if(clientID != null && userLevelID != null){

                        int Client_id = Integer.parseInt(clientID);
                        review.setClient_id(Client_id);
                        ReviewService reviewService = new ReviewService();
                        boolean isSuccess = reviewService.isReviewInsertionSuccessful(review);

                        if (isSuccess) {
                            resp.setStatus(HttpServletResponse.SC_OK);
                            resp.getWriter().write("Review submitted successfully");
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().write("Review submitted unsuccessfully");
                        }
                    }else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("Required Field Missing");
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("User is not authorized to perform this action!");
                }
            }else {
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
