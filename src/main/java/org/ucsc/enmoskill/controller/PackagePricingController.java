package org.ucsc.enmoskill.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

import com.google.gson.*;
import org.ucsc.enmoskill.model.LogoDesignDeliverables;
import org.ucsc.enmoskill.model.PackagePricing;

import static org.ucsc.enmoskill.Services.LogoDesDeliverablesService.*;
import static org.ucsc.enmoskill.Services.PricePackageService.*;

public class PackagePricingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int category = Integer.parseInt(req.getParameter("category"));
        int packageId = Integer.parseInt(req.getParameter("packageId"));
        String type = req.getParameter("type");

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
        switch (category) {
            case 1:
                try {
                    PackagePricing newPackagePricing = gson.fromJson(jsonObject, PackagePricing.class);

//                    newPackagePricing.setcategory(category);
                    newPackagePricing.setPackageId(packageId);
                    newPackagePricing.setType(type);

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

                    System.out.println("price package id : "+result1);


                    if (result1>0){
                        LogoDesignDeliverables newDeliverables = gson.fromJson(deliverables, LogoDesignDeliverables.class);

                        newDeliverables.setPricePackageId(result1);
                        int result2 = insertLDDeliverables(newDeliverables);

                        if (result2>0){
                            resp.setStatus(HttpServletResponse.SC_OK);
                            out.write("price package details inserted successfully");
                            System.out.println("price package details inserted successfully");
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



                    LogoDesignDeliverables newDeliverables = gson.fromJson(deliverables, LogoDesignDeliverables.class);

                    newDeliverables.setPricePackageId(result1);

                    int result2 = insertLDDeliverables(newDeliverables);



                } catch (JsonSyntaxException | JsonIOException e) {
                    throw new RuntimeException(e);
                }


                break;
            case 2:

                System.out.println("illustration not implemented");
                break;
            case 3:

                System.out.println("flyer design not implemented");
                break;
            case 4:

                System.out.println("banner design not implemented");
                break;
            default:

                System.out.println("not implemented");
                break;
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
