package org.ucsc.enmoskill.controller;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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

//    private TokenService.TokenInfo tokenInfo;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//default req is a request of controller

        try {
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);

//        int designerUserId = Integer.parseInt(req.getParameter("UserId"));
            int packageId = Integer.parseInt(req.getParameter("packageId"));

            int designerUserId = Integer.parseInt(tokenInfo.getUserId());

            System.out.println(designerUserId);

            if (tokenService.isTokenValidState(token) == 1){
                if (packageId == 0) {

                    try {
                        // Fetch data based on designerUserId
                        List<Package> packageList = getPackageData(designerUserId);
//                    handleGetResponse(packageList, out, resp);

                        if (packageList != null){
                            resp.setStatus(HttpServletResponse.SC_OK);
                            Gson gson = new Gson();
                            String jsonData = gson.toJson(packageList);
                            out.write(jsonData);
                            System.out.println("Data loaded successfully");
                        } else {
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.write("Data not found");
                            System.out.println("Data not found");
                        }
                    } catch (Exception e) {
                        out.write(e.toString());
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                } else if (packageId > 0) {

                    try {

                        // Fetch data based on packageId
                        Package newPackage = getPackage(packageId);
//                    handleGetResponse(newPackage, out, resp);

                        if (newPackage != null){
                            resp.setStatus(HttpServletResponse.SC_OK);
                            Gson gson = new Gson();
                            String jsonData = gson.toJson(newPackage);
                            out.write(jsonData);
                            System.out.println("Data loaded successfully");
                        } else {
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            out.write("Data not found");
                            System.out.println("Data not found");
                        }
                    } catch (Exception e) {
                        out.write(e.toString());
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Invalid request parameters");
                    System.out.println("Invalid request parameters");
                }
            } else if (tokenService.isTokenValidState(token) == 2) {
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }

        } catch (ExpiredJwtException e) {
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

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//defulat req is a request of controller

        try {
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);

            if (tokenService.isTokenValidState(token) == 1){
//            int packageID = 0;

                int designerUserId = Integer.parseInt(tokenInfo.getUserId());

                try{
                    Gson gson = new Gson();

                    BufferedReader reader =  req.getReader();
                    Package newPackage = gson.fromJson(reader,Package.class);
                    System.out.println(gson.toJson(newPackage));

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
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resultJson.addProperty("message", "Data didn't insert");
                        System.out.println("Data didn't insert");
                    }

                    // Send the JSON object as the response
                    out.write(resultJson.toString());

                } catch (Exception e) {
                    out.write(e.toString());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else if (tokenService.isTokenValidState(token) == 2) {
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }
        } catch (ExpiredJwtException e) {
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

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//defulat req is a request of controller

        TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);

        try {
            if (tokenService.isTokenValidState(token) == 1){
                int designerUserId = Integer.parseInt(tokenInfo.getUserId());
                int packageId = Integer.parseInt(req.getParameter("packageId"));

                try{
                    Gson gson = new Gson();

                    BufferedReader reader = req.getReader();
                    Package newPackage = gson.fromJson(reader,Package.class);
                    newPackage.setDesignerUserId(designerUserId);
                    newPackage.setPackageId(packageId);

                    System.out.println(gson.toJson(newPackage));

                    int result = updatePackageData(newPackage);

                    if (result>0){
                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.write("row updated successfully");
                        System.out.println("row updated successfully");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("row update unsuccessful");
                        System.out.println("row update unsuccessful");
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
        } catch (ExpiredJwtException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }



    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//defulat req is a request of controller

        try {
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);

            if (tokenService.isTokenValidState(token) == 1) {
//            int designerUserId = 1;

                try {
                    Gson gson = new Gson();

                    int packageId = Integer.parseInt(req.getParameter("packageId"));
                    System.out.println(packageId);

                    // extract designer id by query parameters
//                int designerUserId = Integer.parseInt(req.getParameter("UserId"));

                    int designerUserId = Integer.parseInt(tokenInfo.getUserId());

                    BufferedReader reader = req.getReader();
                    Package newPackage = gson.fromJson(reader, Package.class);
                    newPackage.setPackageId(packageId);
                    newPackage.setDesignerUserId(designerUserId);

                    int result = deletePackageData(newPackage);

                    if (result > 0) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.write("data deleted successfully");
                        System.out.println("data deleted successfully");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("data deletion unsuccessful");
                        System.out.println("data deletion unsuccessful");
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
        } catch (ExpiredJwtException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }




    }


}
