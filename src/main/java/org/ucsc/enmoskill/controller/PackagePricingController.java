package org.ucsc.enmoskill.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.*;

import com.google.gson.reflect.TypeToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.ucsc.enmoskill.Services.PackageDeliverablesService;
import org.ucsc.enmoskill.Services.PricePackageService;
import org.ucsc.enmoskill.model.*;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.utils.TokenService;


public class PackagePricingController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        Gson gson = new Gson();
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        try {
            tokenInfo = tokenService.getTokenInfo(token);

            int packageId = Integer.parseInt(req.getParameter("packageId"));

            //check validity of token
            if (tokenService.isTokenValidState(token) == 1){


                try {
                    PricePackageService newService = new PricePackageService();
                    List<PackagePricing> priceList;
                    priceList =   newService.getPricePackage(packageId);

                    if (priceList != null){
                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.write(gson.toJson(priceList));
                        System.out.println("Data loaded successfully");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("Data not found");
                        System.out.println("Data not found");
                    }
                } catch (Exception e) {
                    out.write(e.toString());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

            } else if (tokenService.isTokenValidState(token) == 2) {
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }
        }catch (ExpiredJwtException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }





    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        try {
            tokenInfo = tokenService.getTokenInfo(token);


//        int category = Integer.parseInt(req.getParameter("category"));
            int packageId = Integer.parseInt(req.getParameter("packageId"));

            if (tokenService.isTokenValidState(token) == 1){

                if (tokenInfo.isDesigner()){
                    JsonObject responseJson = new JsonObject();

                    // Phrase the json string into class
                    BufferedReader reader = req.getReader();
                    PackagePricing pricing = gson.fromJson(reader,PackagePricing.class);

                    // insert pricing data
                    PricePackageService pricePackageService = new PricePackageService();

                    try{
                        int result1 = pricePackageService.insertPricePackageData(pricing);
                        pricing.setPricePackageId(result1);

                        DeliverablesModel deliverables = pricing.getDel();
                        deliverables.setPricePackageId(result1);
                        pricing.setDel(deliverables);

                        if (result1 > 0){

                            System.out.println(result1);

                            try {
                                //insert deliverables data
                                PackageDeliverablesService deliverablesService = new PackageDeliverablesService();
                                int result2 = deliverablesService.insertPackageDeliverables(deliverables);

                                responseJson.addProperty("pricePackageId", result1);

                                if (result2 > 0){
                                    responseJson.addProperty("deliverablesId", result2);

                                    responseJson.addProperty("message", "Data inserted successfully");
                                    resp.setStatus(HttpServletResponse.SC_OK);
                                    out.write(gson.toJson(responseJson));
                                    System.out.println("Data inserted successfully");
                                } else {
                                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                    out.write("Data insertion unsuccessful");
                                    System.out.println("Data insertion unsuccessful");
                                }
                            } catch (Exception e) {
                                out.write(e.toString());
                                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            }


                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("Data insertion unsuccessful");
                            System.out.println("Data insertion unsuccessful");
                        }
                    } catch (Exception e) {
                        out.write(e.toString());
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    out.write("access denied");
                    System.out.println("access denied");
                }



            } else if (tokenService.isTokenValidState(token) == 2) {
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }
        }catch (ExpiredJwtException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }



    }



    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        try {
            tokenInfo = tokenService.getTokenInfo(token);


            int pricePackageId = Integer.parseInt(req.getParameter("pricePackageId"));
            int deliverablesId = Integer.parseInt(req.getParameter("deliverablesId"));

            System.out.println(deliverablesId);
            if (tokenService.isTokenValidState(token) == 1){

                if (tokenInfo.isDesigner()){

                    try {
                        // extract the request body
                        BufferedReader reader = req.getReader();
                        PackagePricing pricing = gson.fromJson(reader, PackagePricing.class);
                        pricing.setPricePackageId(pricePackageId);

                        DeliverablesModel deliverables = pricing.getDel();
                        deliverables.setPricePackageId(pricePackageId);
                        deliverables.setDeliverablesId(deliverablesId);
                        pricing.setDel(deliverables);


                        // update pricing data
                        PricePackageService pricePackageService = new PricePackageService();
                        int result1 = pricePackageService.updatePricePackageData(pricing);

                        if (result1 > 0){

                            // update deliverables data
                            PackageDeliverablesService deliverablesService = new PackageDeliverablesService();
                            int result2 = deliverablesService.updatePackageDeliverables(pricing.getDel());

                            if (result2 > 0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                out.write("Data updated successfully");
                                System.out.println("Data updated successfully");
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.write("Data del update unsuccessful");
                                System.out.println("Data del update unsuccessful");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("Data update unsuccessful");
                            System.out.println("Data update unsuccessful");
                        }
                    } catch (Exception e) {
                        out.write(e.toString());
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }



            } else if (tokenService.isTokenValidState(token) == 2) {
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }
        }catch (ExpiredJwtException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e){
            out.write(e.toString());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();


    }
}