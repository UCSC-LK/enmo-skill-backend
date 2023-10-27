package org.ucsc.enmoskill.controller;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.ucsc.enmoskill.model.Package;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.ucsc.enmoskill.Services.PackageService.*;

public class PackageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("hello world 1");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // create an array using all the cookies
        Cookie[] cookies = req.getCookies();
        int designerUserId = 1;

        // // getting the designer id form the cookie
//            for (Cookie cookie:
//                 cookies) {
//                if (cookie.getName().equals("user")){
//                    designerUserId = Integer.parseInt(cookie.getValue());
//                }
//            }

        List<Package> packageList;
        packageList = getPackageData(designerUserId);

        if (!packageList.isEmpty()) {
            // You have successfully retrieved the userId from the cookie
            // Now, you can use it as needed.




            Gson gsonPackageList = new Gson();
            String json = gsonPackageList.toJson(packageList);

            resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
            resp.setStatus(HttpServletResponse.SC_OK);
            out.write(json); // Write the JSON string as the response
            System.out.println("data loaded successfully");

        } else {
            // Handle the case where the "userId" cookie is not found
            // You might want to redirect the user to a login page or take some other action.
            resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Data not found");
            System.out.println("Data not found");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int packageID = 0;
//        String title = null;
//        String description = null;
//        String category = null;
//        String coverUrl = null;
//        int clicks = 0;
//        int orders = 0;
//        String cancellations = "0%";
//        String status = "active";
        int designerUserId = 1;
//
//
//        // // getting the designer id form the cookie
////            for (Cookie cookie:
////                 cookies) {
////                if (cookie.getName().equals("user")){
////                    designerUserId = Integer.parseInt(cookie.getValue());
////                }
////            }
//
        // generate a number between 10 and 100000 as packageID
        packageID = (int)(Math.random()*(100000-10+1)+10);
        System.out.println(packageID);
//
//        title = req.getParameter("title");
//        description = req.getParameter("description");
//        category = req.getParameter("category");
//
//        Package newPackage = new Package(packageID, title, description, category, coverUrl, clicks, orders, cancellations, status, designerUserId);

        try{
            Gson gson = new Gson();

            BufferedReader reader =  req.getReader();
            Package newPackage = gson.fromJson(reader,Package.class);

            newPackage.setPackageId(packageID);
            newPackage.setDesignerUserId(designerUserId);


            int result = insertPackageData(newPackage);

            if (result>0){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                out.write("data inserted successfully");
                System.out.println("data inserted successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                out.write("data didn't inserted");
                System.out.println("data didn't inserted");
            }
        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            throw new RuntimeException(e);
        }




    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int designerUserId = 1;


        // // getting the designer id form the cookie
//            for (Cookie cookie:
//                 cookies) {
//                if (cookie.getName().equals("user")){
//                    designerUserId = Integer.parseInt(cookie.getValue());
//                }
//            }

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
                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                out.write("row updated successfully");
                System.out.println("row updated successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                out.write("row update unsuccessful");
                System.out.println("row update unsuccessful");
            }

        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            throw new RuntimeException(e);
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

        int designerUserId = 1;

        try {
            Gson gson = new Gson();

            int packageId = Integer.parseInt(req.getParameter("packageId"));
            System.out.println(packageId);

            BufferedReader reader = req.getReader();
            Package newPackage = gson.fromJson(reader,Package.class);
            newPackage.setPackageId(packageId);
            newPackage.setDesignerUserId(designerUserId);

            int result = deletePackageData(newPackage);

            if (result>0){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                out.write("data deleted successfully");
                System.out.println("data deleted successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
                out.write("data deletion unsuccessful");
                System.out.println("data deletion unsuccessful");
            }
        } catch (NumberFormatException | IOException | JsonSyntaxException | JsonIOException e) {
            throw new RuntimeException(e);
        }

//        int packageId = Integer.parseInt(req.getParameter("packageId"));
//
//        Package newPackage = new Package(packageId);





    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        resp.addHeader("Access-Control-Max-Age", "3600");
        resp.setStatus(HttpServletResponse.SC_OK);
    }



}
