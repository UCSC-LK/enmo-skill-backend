package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.ucsc.enmoskill.Services.ProposalDELETESer;
import org.ucsc.enmoskill.Services.ProposalGETSer;
import org.ucsc.enmoskill.Services.ProposalPOSTSer;
import org.ucsc.enmoskill.Services.ProposalUPDATESer;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Pro_CR;
import org.ucsc.enmoskill.model.ProposalModel;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.Arrays;

import static java.lang.System.out;

public class ProposalController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Set CORS headers to allow requests from any origin with credentials
        resp.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
        resp.setHeader("Access-Control-Allow-Credentials", "true");


        resp.setContentType("application/json");

        Connection connection = DatabaseConnection.initializeDatabase();

        Pro_CR proBRlist = new Pro_CR(req);

        out.println("ProposalID: " + proBRlist.getProposalid());
        out.println("UserID: " + proBRlist.getUserid());
        out.println("Role: " + proBRlist.getRole());
        out.println("req: " + req);


        // Get cookies from the request
        Cookie[] cookies = req.getCookies();
        out.println("cookie: " + Arrays.toString(cookies));
        // Check if cookies are present
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWTToken")) {
                    String jwtToken = cookie.getValue();

                    // Now you have the JWTToken value, and you can use it as needed
                    out.println("JWTToken: " + jwtToken);

                    try {
                        JWT parsedJWT = JWTParser.parse(jwtToken);
                        JWTClaimsSet claimsSet = parsedJWT.getJWTClaimsSet();

                        // Access individual claims
                        String subject = claimsSet.getSubject();
                        String userLevelID = claimsSet.getStringClaim("userLevelID");
                        String userID = claimsSet.getStringClaim("userID");

                        System.out.println("User Level ID: " + userLevelID);
                        System.out.println("User ID: " + userID);


                        if(userLevelID != null && userID != null) {
                            if ("1".equals(userLevelID)){
                                if (userID == null){
                                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                    resp.getWriter().write("User ID is Required!");
                                }else{
                                    if(proBRlist.getProposalid() == null) {
                                        ProposalGETSer service = new ProposalGETSer(resp);
                                        service.GetAllProposals(connection ,userID);
                                    }else{
                                        ProposalGETSer service = new ProposalGETSer(resp);
                                        service.GetProposal(connection ,proBRlist.getProposalid(),userID,resp );
                                    }

                                }
                            }
                        }else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().write("Role is Required!");
                        }
                    } catch (JwtException e) {
                        // Handle the exception (e.g., log it, return an error response, etc.)
                        System.out.println("Error decoding JWT: " + e.getMessage());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("No cookies found in the request");
        }
   }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try{
            // Create a Gson instance
            Gson gson = new Gson();

            // Read JSON data from the request body
            BufferedReader reader = req.getReader();
            ProposalModel proposal = gson.fromJson(reader, ProposalModel.class);

            Pro_CR proBRlist = new Pro_CR(req);

            out.println("getDuration: " + proposal.getDuration());
            out.println("getDescription: " + proposal.getDescription());
            out.println("getBudget: " + proposal.getBudget());
            out.println("getDate: " + proposal.getDate());
            out.println("getUserID: " + proposal.getUserID());

            // Get cookies from the request
            Cookie[] cookies = req.getCookies();
            out.println("cookie: " + Arrays.toString(cookies));
            // Check if cookies are present
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("JWTToken")) {
                        String jwtToken = cookie.getValue();

                        // Now you have the JWTToken value, and you can use it as needed
                        out.println("JWTToken: " + jwtToken);

                        try {
                            JWT parsedJWT = JWTParser.parse(jwtToken);
                            JWTClaimsSet claimsSet = parsedJWT.getJWTClaimsSet();

                            // Access individual claims
                            String subject = claimsSet.getSubject();
                            String userLevelID = claimsSet.getStringClaim("userLevelID");
                            String userID = claimsSet.getStringClaim("userID");

                            System.out.println("User Level ID: " + userLevelID);
                            System.out.println("User ID: " + userID);

                            if (userID!=null && userLevelID != null  && proposal.getBudget()!=null && proposal.getDescription()!=null && proposal.getDuration()!=null){

                                ProposalPOSTSer proposalPOSTSer = new ProposalPOSTSer();
                                boolean isSuccess = proposalPOSTSer.isInsertionSuccessful(proposal , proBRlist ,userID);

                                if (isSuccess) {
                                    resp.setStatus(HttpServletResponse.SC_OK);
                                    resp.getWriter().write("Proposal submitted successfully");
                                } else {
                                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                    resp.getWriter().write("Proposal submitted unsuccessfully");
                                }
                            }else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                resp.getWriter().write("Required Field Missing");
                            }

                        } catch (JwtException e) {
                            // Handle the exception (e.g., log it, return an error response, etc.)
                            System.out.println("Error decoding JWT: " + e.getMessage());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }else{
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println("No cookies found in the request");
            }

        }catch (Exception e) {
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

        Cookie[] cookies = req.getCookies();
        out.println("cookie: " + Arrays.toString(cookies));
        // Check if cookies are present
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWTToken")) {
                    String jwtToken = cookie.getValue();

                    // Now you have the JWTToken value, and you can use it as needed
                    out.println("JWTToken: " + jwtToken);

                    try {
                        JWT parsedJWT = JWTParser.parse(jwtToken);
                        JWTClaimsSet claimsSet = parsedJWT.getJWTClaimsSet();

                        // Access individual claims
                        String subject = claimsSet.getSubject();
                        String userLevelID = claimsSet.getStringClaim("userLevelID");
                        String userID = claimsSet.getStringClaim("userID");

                        System.out.println("User Level ID: " + userLevelID);
                        System.out.println("User ID: " + userID);

                        if(proBRlist.getProposalid() == null) {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().write("Proposal ID is Required!");
                        }else{
                            ProposalDELETESer service = new ProposalDELETESer(resp);
                            service.DeleteProposal(connection ,proBRlist.getProposalid(),resp );
                        }


                    } catch (JwtException e) {
                        // Handle the exception (e.g., log it, return an error response, etc.)
                        System.out.println("Error decoding JWT: " + e.getMessage());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("No cookies found in the request");
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

        Cookie[] cookies = req.getCookies();
        out.println("cookie: " + Arrays.toString(cookies));
        // Check if cookies are present
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWTToken")) {
                    String jwtToken = cookie.getValue();

                    // Now you have the JWTToken value, and you can use it as needed
                    out.println("JWTToken: " + jwtToken);

                    try {
                        JWT parsedJWT = JWTParser.parse(jwtToken);
                        JWTClaimsSet claimsSet = parsedJWT.getJWTClaimsSet();

                        // Access individual claims
                        String subject = claimsSet.getSubject();
                        String userLevelID = claimsSet.getStringClaim("userLevelID");
                        String userID = claimsSet.getStringClaim("userID");

                        System.out.println("User Level ID: " + userLevelID);
                        System.out.println("User ID: " + userID);

                        if(proBRlist.getProposalid() == null) {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().write("Proposal ID is Required!");
                        }else{
                            ProposalUPDATESer service = new ProposalUPDATESer(resp);
                            service.UpdateProposal(connection ,proBRlist.getProposalid(),proposal ,resp );
                        }


                    } catch (JwtException e) {
                        // Handle the exception (e.g., log it, return an error response, etc.)
                        System.out.println("Error decoding JWT: " + e.getMessage());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("No cookies found in the request");
        }

    }
}
