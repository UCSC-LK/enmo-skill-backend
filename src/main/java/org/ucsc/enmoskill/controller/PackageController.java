package org.ucsc.enmoskill.controller;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.ucsc.enmoskill.Services.PackageService.*;

public class PackageController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//defulat req is a request of controller

        tokenInfo = tokenService.getTokenInfo(token);

//        int designerUserId = Integer.parseInt(req.getParameter("UserId"));
        int packageId = Integer.parseInt(req.getParameter("packageId"));

        int designerUserId = Integer.parseInt(tokenInfo.getUserId());

        System.out.println(designerUserId);

        if (tokenService.isTokenValid(token)){
            if (packageId == 0) {
                // Fetch data based on designerUserId
                List<Package> packageList = getPackageData(designerUserId);
                handleGetResponse(packageList, out, resp);
            } else if (packageId > 0) {
                // Fetch data based on packageId
                Package newPackage = getPackage(packageId);
                handleGetResponse(newPackage, out, resp);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Invalid request parameters");
                System.out.println("Invalid request parameters");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }





    }

    private void handleGetResponse(Object data, PrintWriter out, HttpServletResponse resp) {
        if (data != null) {
            Gson gson = new Gson();
            String jsonData = gson.toJson(data);

            resp.setStatus(HttpServletResponse.SC_OK);
            out.write(jsonData);
            System.out.println("Data loaded successfully");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.write("Data not found");
            System.out.println("Data not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//defulat req is a request of controller

        tokenInfo = tokenService.getTokenInfo(token);

        if (tokenService.isTokenValid(token)){
            int packageID = 0;
//        String title = null;
//        String description = null;
//        String category = null;
//        String coverUrl = null;
//        int clicks = 0;
//        int orders = 0;
//        String cancellations = "0%";
//        String status = "active";
//        int designerUserId = 1;
//
//
            // extract designer rif by query parameters
//            int designerUserId = Integer.parseInt(req.getParameter("UserId"));

            int designerUserId = Integer.parseInt(tokenInfo.getUserId());

            try{
                Gson gson = new Gson();

                BufferedReader reader =  req.getReader();
                Package newPackage = gson.fromJson(reader,Package.class);

//            newPackage.setPackageId(packageID);
                newPackage.setDesignerUserId(designerUserId);

                System.out.println(newPackage.getPackageId());


                int result = insertPackageData(newPackage);

                // Create a JSON object to represent the result
                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("result", result);


                if (result>0){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resultJson.addProperty("message", "Data inserted successfully");
                    System.out.println("Data inserted successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resultJson.addProperty("message", "Data didn't insert");
                    System.out.println("Data didn't insert");
                }

                // Send the JSON object as the response
                out.write(resultJson.toString());

            } catch (IOException | JsonSyntaxException | JsonIOException e) {
                throw new RuntimeException(e);
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
        String token = tokenService.getTokenFromHeader(req);//defulat req is a request of controller

        tokenInfo = tokenService.getTokenInfo(token);

        if (tokenService.isTokenValid(token)){
            //        int designerUserId = 1;


            // extract designer id by query parameters
//            int designerUserId = Integer.parseInt(req.getParameter("UserId"));

            int designerUserId = Integer.parseInt(tokenInfo.getUserId());

            try{
                Gson gson = new Gson();

                int packageId = Integer.parseInt(req.getParameter("packageId"));
                System.out.println(packageId);

                BufferedReader reader = req.getReader();
                Package newPackage = gson.fromJson(reader,Package.class);
                newPackage.setPackageId(packageId);
                newPackage.setDesignerUserId(designerUserId);

                int result = updatePackageData(newPackage);

                if (result>0){
                    resp.setStatus(HttpServletResponse.SC_OK);
//                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                    out.write("row updated successfully");
                    System.out.println("row updated successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                    out.write("row update unsuccessful");
                    System.out.println("row update unsuccessful");
                }

            } catch (IOException | JsonSyntaxException | JsonIOException e) {
                throw new RuntimeException(e);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }



//        int result = updatePackageData(newPackage);

//        if (result>0){
//            resp.setStatus(HttpServletResponse.SC_OK);
//            out.write("data deleted successfully");
//            System.out.println("data deleted successfully");
//        } else {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            out.write("data deletion unsuccessful");
//            System.out.println("data deletion unsuccessful");
//        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//defulat req is a request of controller

        tokenInfo = tokenService.getTokenInfo(token);

        if (tokenService.isTokenValid(token)){
//            int designerUserId = 1;

            try {
                Gson gson = new Gson();

                int packageId = Integer.parseInt(req.getParameter("packageId"));
                System.out.println(packageId);

                // extract designer id by query parameters
//                int designerUserId = Integer.parseInt(req.getParameter("UserId"));

                int designerUserId = Integer.parseInt(tokenInfo.getUserId());

                BufferedReader reader = req.getReader();
                Package newPackage = gson.fromJson(reader,Package.class);
                newPackage.setPackageId(packageId);
                newPackage.setDesignerUserId(designerUserId);

                int result = deletePackageData(newPackage);

                if (result>0){
                    resp.setStatus(HttpServletResponse.SC_OK);
//                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                    out.write("data deleted successfully");
                    System.out.println("data deleted successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                    out.write("data deletion unsuccessful");
                    System.out.println("data deletion unsuccessful");
                }
            } catch (NumberFormatException | IOException | JsonSyntaxException | JsonIOException e) {
                throw new RuntimeException(e);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }

//

//        int packageId = Integer.parseInt(req.getParameter("packageId"));
//
//        Package newPackage = new Package(packageId);





    }

//    @Override
//    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
////        resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
////        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
////        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
////        resp.addHeader("Access-Control-Max-Age", "3600");
//        resp.setStatus(HttpServletResponse.SC_OK);
//    }



}
