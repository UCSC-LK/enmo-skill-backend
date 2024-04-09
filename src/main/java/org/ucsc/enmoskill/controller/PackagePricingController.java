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
import org.ucsc.enmoskill.Services.PackageDeliverablesService;
import org.ucsc.enmoskill.Services.PricePackageService;
import org.ucsc.enmoskill.model.*;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.utils.TokenService;

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

        Gson gson = new Gson();
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        int packageId = Integer.parseInt(req.getParameter("packageId"));

        //check validy of token
        if (tokenService.isTokenValid(token)){

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
                deliverables = jsonObject.getAsJsonObject("del");

                // Remove "deliverables" from the original JsonObject
                jsonObject.remove("deliverables");

                // Now, you have two separate JsonObjects
//                System.out.println("Original Object Without Deliverables: " + jsonObject);
//                System.out.println("Deliverables Object: " + deliverables);
            }
//
//
            Gson gson = new Gson();
            PackagePricing newPackagePricing = gson.fromJson(jsonObject, PackagePricing.class);
            newPackagePricing.setPackageId(packageId);
            newPackagePricing.setDel(gson.fromJson(deliverables, DeliverablesModel.class));

            PricePackageService service = new PricePackageService();
            int result1 = service.insertPricePackageData(newPackagePricing);


            if (result1 > 0){
//                JsonObject resultJson = new JsonObject();
//                resultJson.addProperty("pricePackageId", result1);
//                PackageDeliverables newDeliverables = gson.fromJson(deliverables, PackageDeliverables.class);
//                newDeliverables.setPricePackageId(result1);
                newPackagePricing.setPricePackageId(result1);

                PackageDeliverablesService service2 = new PackageDeliverablesService();
                int result2 = service2.insertPackageDeliverables(newPackagePricing);

                if (result2>0){
                    resp.setStatus(HttpServletResponse.SC_OK);
//                    resultJson.addProperty("message", "price package details inserted successfully");
////                            out.write("price package details inserted successfully");
                    System.out.println("price package details inserted successfully");
                    out.print(result2);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("price package details did not inserted");
                    System.out.println("price package details did not inserted");
                }
            }  else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("price package details did not inserted");
                    System.out.println("price package details did not inserted");
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
//        int category = Integer.parseInt(req.getParameter("category"));
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
                deliverables = jsonObject.getAsJsonObject("del");

                // Remove "deliverables" from the original JsonObject
                jsonObject.remove("deliverables");

                // Now, you have two separate JsonObjects
                System.out.println("Original Object Without Deliverables: " + jsonObject);
                System.out.println("Deliverables Object: " + deliverables);
            }

            Gson gson = new Gson();

            PackagePricing newPackagePricing = gson.fromJson(jsonObject, PackagePricing.class);
            newPackagePricing.setPackageId(packageId);
            newPackagePricing.setPricePackageId(pricePackageId);
            newPackagePricing.setDel(gson.fromJson(deliverables,DeliverablesModel.class));

            int result1 = updatePricePackageData(newPackagePricing);

            if (result1 > 0) {
//                PackageDeliverables newDeliverables = gson.fromJson(deliverables, PackageDeliverables.class);
//                newDeliverables.setPricePackageId(pricePackageId);
//
//                System.out.println(newDeliverables.getMockup());
//                System.out.println(newDeliverables.getDeliverablesId());

                PackageDeliverablesService service = new PackageDeliverablesService();
                int result2 = service.updatePackageDeliverables(newPackagePricing);

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