package org.ucsc.enmoskill.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.*;


import org.ucsc.enmoskill.model.*;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.utils.TokenService;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import static org.ucsc.enmoskill.Services.BannerDesDeliverablesService.*;
import static org.ucsc.enmoskill.Services.FlyerDesDeliverablesService.*;
import static org.ucsc.enmoskill.Services.IllustrationDeliverablesService.*;
import static org.ucsc.enmoskill.Services.LogoDesDeliverablesService.*;
import static org.ucsc.enmoskill.Services.PackageService.*;
import static org.ucsc.enmoskill.Services.PricePackageService.*;

public class PackagePricingController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        int packageId = Integer.parseInt(req.getParameter("packageId"));

        if (tokenService.isTokenValid(token)){
            // fetch the category of the price package
            Package pkgObj = getPackage(packageId);
            int category = pkgObj.getCategory();
            System.out.println("Category is :"+category);

            StringBuilder sb = fetchData(packageId, category);

            if (sb == null){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Data not found");
                System.out.println("Data not found");
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write(String.valueOf(sb));
                System.out.println("Data loaded successfully");
            }



//            resp.setStatus(HttpServletResponse.SC_OK);
//            out.write(String.valueOf(jsonObj));
//            System.out.println("Data loaded successfully");
//        } else {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            out.write("Data not found");
//            System.out.println("Data not found");
//        }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }




    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);


        int category = Integer.parseInt(req.getParameter("category"));
        int packageId = Integer.parseInt(req.getParameter("packageId"));

        if (tokenService.isTokenValid(token)){
            //        String type = req.getParameter("type");

            StringBuilder requestBody = new StringBuilder();
            BufferedReader reader = req.getReader();

            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            System.out.println(requestBody);
            JsonObject jsonObject = null;
            JsonObject deliverables = null;

            // Parse the JSON string
            JsonElement jsonElement = JsonParser.parseString(requestBody.toString());

            // Check if it's a JSON object
            if (jsonElement.isJsonObject()) {
                // Convert to a JsonObject
                jsonObject = jsonElement.getAsJsonObject();

                // Create separate JsonObjects for "deliverables" and the rest
                deliverables = jsonObject.getAsJsonObject("deliverables");

                // Remove "deliverables" from the original JsonObject
                jsonObject.remove("deliverables");

                // Now, you have two separate JsonObjects
                System.out.println("Original Object Without Deliverables: " + jsonObject);
                System.out.println("Deliverables Object: " + deliverables);
            }
//
//
            Gson gson = new Gson();
            PackagePricing newPackagePricing = gson.fromJson(jsonObject, PackagePricing.class);

            switch (category) {
                case 1:
                    try {

//                    newPackagePricing.setcategory(category);
                        newPackagePricing.setPackageId(packageId);
//                    newPackagePricing.setType(type);

                        System.out.println(newPackagePricing.getType());

//                    System.out.println(newPackagePricing.getPricePackageId());
//                    System.out.println(newPackagePricing.getType());
//
//                    System.out.println(newPackagePricing.getDeliveryDuration());
//
//                    System.out.println(newPackagePricing.getNoOfRevisions());
//                    System.out.println(newPackagePricing.getPrice());
//                    System.out.println(newPackagePricing.getPackageId());
//                    System.out.println(newPackagePricing.getNoOfConcepts());

                        int result1 = insertPricePackageData(newPackagePricing);

                        JsonObject resultJson = new JsonObject();
                        resultJson.addProperty("pricePackageId", result1);

                        System.out.println("price package id : "+result1);


                        if (result1>0){
                            LogoDesignDeliverables newDeliverables = gson.fromJson(deliverables, LogoDesignDeliverables.class);

                            newDeliverables.setPricePackageId(result1);
                            int result2 = insertLDDeliverables(newDeliverables);

                            if (result2>0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resultJson.addProperty("message", "price package details inserted successfully");
//                            out.write("price package details inserted successfully");
                                System.out.println("price package details inserted successfully");
                                out.print(resultJson.toString());
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.write("price package details did not inserted");
                                System.out.println("price package details did not inserted");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("price package details did not inserted");
                            System.out.println("price package details did not inserted");
                        }


                    } catch (JsonSyntaxException | JsonIOException e) {
                        throw new RuntimeException(e);
                    }


                    break;
                case 2:

                    try {

//                    newPackagePricing.setcategory(category);
                        newPackagePricing.setPackageId(packageId);
//
                        int result1 = insertPricePackageData(newPackagePricing);

                        JsonObject resultJson = new JsonObject();
                        resultJson.addProperty("pricePackageId", result1);

                        System.out.println("price package id : "+result1);


                        if (result1>0){
                            IllustrationDeliverables newDeliverables = gson.fromJson(deliverables, IllustrationDeliverables.class);

                            newDeliverables.setPricePackageID(result1);

                            System.out.println(newDeliverables.getBackground_Scene());
                            int result2 = insertIDeliverables(newDeliverables);

                            if (result2>0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resultJson.addProperty("message", "price package details inserted successfully");
//                            out.write("price package details inserted successfully");
                                System.out.println("price package details inserted successfully");
                                out.print(resultJson.toString());
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.write("price package details did not inserted");
                                System.out.println("price package details did not inserted");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("price package details did not inserted");
                            System.out.println("price package details did not inserted");
                        }

                    } catch (JsonSyntaxException | JsonIOException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case 3:


                    try {

                        newPackagePricing.setPackageId(packageId);

                        int result1 = insertPricePackageData(newPackagePricing);

                        JsonObject resultJson = new JsonObject();
                        resultJson.addProperty("pricePackageId", result1);

                        System.out.println("price package id : "+result1);


                        if (result1>0){
                            FlyerDesignDeliverables newDeliverables = gson.fromJson(deliverables, FlyerDesignDeliverables.class);

                            newDeliverables.setPricePackageID(result1);
                            int result2 = insertFDDeliverables(newDeliverables);

                            if (result2>0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resultJson.addProperty("message", "price package details inserted successfully");
//                            out.write("price package details inserted successfully");
                                System.out.println("price package details inserted successfully");
                                out.print(resultJson.toString());
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.write("price package details did not inserted");
                                System.out.println("price package details did not inserted");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("price package details did not inserted");
                            System.out.println("price package details did not inserted");
                        }

                    } catch (JsonSyntaxException | JsonIOException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case 4:

                    try {

                        newPackagePricing.setPackageId(packageId);

                        int result1 = insertPricePackageData(newPackagePricing);

                        JsonObject resultJson = new JsonObject();
                        resultJson.addProperty("pricePackageId", result1);

                        System.out.println("price package id : "+result1);


                        if (result1>0){
                            BannerDesignDeliverables newDeliverables = gson.fromJson(deliverables, BannerDesignDeliverables.class);

                            newDeliverables.setPricePackageID(result1);
                            int result2 = insertBDDeliverables(newDeliverables);

                            if (result2>0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resultJson.addProperty("message", "price package details inserted successfully");
//                            out.write("price package details inserted successfully");
                                System.out.println("price package details inserted successfully");
                                out.print(resultJson.toString());
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.write("price package details did not inserted");
                                System.out.println("price package details did not inserted");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("price package details did not inserted");
                            System.out.println("price package details did not inserted");
                        }

                    } catch (JsonSyntaxException | JsonIOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:

                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Invalid category");
                    System.out.println("Invalid category");
                    break;
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        int pricePackageId = Integer.parseInt(req.getParameter("pricePackageId"));
        int category = Integer.parseInt(req.getParameter("category"));
        int packageId = Integer.parseInt(req.getParameter("packageId"));

        if (tokenService.isTokenValid(token)){
            //        String type = req.getParameter("type");

            StringBuilder requestBody = new StringBuilder();
            BufferedReader reader = req.getReader();

            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            System.out.println(requestBody);
            JsonObject jsonObject = null;
            JsonObject deliverables = null;

            // Parse the JSON string
            JsonElement jsonElement = JsonParser.parseString(requestBody.toString());

            // Check if it's a JSON object
            if (jsonElement.isJsonObject()) {
                // Convert to a JsonObject
                jsonObject = jsonElement.getAsJsonObject();

                // Create separate JsonObjects for "deliverables" and the rest
                deliverables = jsonObject.getAsJsonObject("deliverables");

                // Remove "deliverables" from the original JsonObject
                jsonObject.remove("deliverables");

                // Now, you have two separate JsonObjects
                System.out.println("Original Object Without Deliverables: " + jsonObject);
                System.out.println("Deliverables Object: " + deliverables);
            }

            Gson gson = new Gson();

            PackagePricing newPackagePricing = gson.fromJson(jsonObject, PackagePricing.class);

//                    newPackagePricing.setcategory(category);
            newPackagePricing.setPackageId(packageId);
//                    newPackagePricing.setType(type);
            newPackagePricing.setPricePackageId(pricePackageId);

            int result1 = updatePricePackageData(newPackagePricing);

//                    System.out.println("price package id : "+result1);
            switch (category) {
                case 1:
                    try {

                        if (result1>0){
                            LogoDesignDeliverables newDeliverables1 = gson.fromJson(deliverables, LogoDesignDeliverables.class);

                            newDeliverables1.setPricePackageId(pricePackageId);
                            int result2 = updateLDDeliverables(newDeliverables1);

                            if (result2>0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                out.write("price package details updated successfully");
                                System.out.println("price package details updated successfully");
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.write("price package details did not updated");
                                System.out.println("price package details did not updated");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("price package details did not updated");
                            System.out.println("price package details did not updated");
                        }

                    } catch (JsonSyntaxException | JsonIOException e) {
                        throw new RuntimeException(e);
                    }


                    break;
                case 2:

                    try{

                        if (result1>0){
                            IllustrationDeliverables newDeliverables2 = gson.fromJson(deliverables, IllustrationDeliverables.class);


                            newDeliverables2.setPricePackageID(pricePackageId);
                            int result2 = updateIllustDeliverables(newDeliverables2);

                            if (result2>0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                out.write("price package details updated successfully");
                                System.out.println("price package details updated successfully");
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.write("price package details did not updated");
                                System.out.println("price package details did not updated");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("price package details did not updated");
                            System.out.println("price package details did not updated");
                        }

                    } catch (JsonSyntaxException | JsonIOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 3:

                    try{

                        if (result1>0){
                            FlyerDesignDeliverables newDeliverables3 = gson.fromJson(deliverables, FlyerDesignDeliverables.class);

                            newDeliverables3.setPricePackageID(pricePackageId);
                            int result2 = updateFDDeliverables(newDeliverables3);

                            if (result2>0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                out.write("price package details updated successfully");
                                System.out.println("price package details updated successfully");
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.write("price package details did not updated");
                                System.out.println("price package details did not updated");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("price package details did not updated");
                            System.out.println("price package details did not updated");
                        }

                    } catch (JsonSyntaxException | JsonIOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 4:

                    try{

                        if (result1>0){
                            BannerDesignDeliverables newDeliverables4 = gson.fromJson(deliverables, BannerDesignDeliverables.class);

                            newDeliverables4.setPricePackageID(pricePackageId);
                            int result2 = updateBDDeliverables(newDeliverables4);

                            if (result2>0){
                                resp.setStatus(HttpServletResponse.SC_OK);
                                out.write("price package details updated successfully");
                                System.out.println("price package details updated successfully");
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                out.write("price package details did not updated");
                                System.out.println("price package details did not updated");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            out.write("price package details did not updated");
                            System.out.println("price package details did not updated");
                        }

                    } catch (JsonSyntaxException | JsonIOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                default:

                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("invalid type");
                    System.out.println("invalid type");
                    break;
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();


    }
}