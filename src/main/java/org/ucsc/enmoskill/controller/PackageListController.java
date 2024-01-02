package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.model.Package;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.ucsc.enmoskill.Services.PackageListService.*;
import static org.ucsc.enmoskill.Services.PricePackageService.*;

public class PackageListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int category = Integer.parseInt(req.getParameter("category"));


        if (category>4 || category<0){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Data not found");
            System.out.println("Data not found");
        } else {
            StringBuilder jsonObj = new StringBuilder("[");

            Gson gson = new Gson();

            List<Package> packageList = getAllPackages(category);
            if (!packageList.isEmpty()){
                for (Package newpackage:packageList) {
                    int packageId = newpackage.getPackageId();

                    float lowestPrice = getBronzePrice(packageId);
                    float highestPrice = getPlatinumPrice(packageId);

                    String pkg = gson.toJson(newpackage);
                    StringBuilder aPackage = new StringBuilder(pkg);

                    aPackage.insert(aPackage.length()-1, ", \"starterPrice\":"+lowestPrice);
                    aPackage.insert(aPackage.length()-1, ", \"highestPrice\":"+highestPrice);


                    jsonObj.append(aPackage);
                    jsonObj.append(",");
                    System.out.println(aPackage);

//                        System.out.println(packageId+" "+lowestPrice);


                }
                int lastIndex = jsonObj.length()-1;
                jsonObj.deleteCharAt(lastIndex);
                jsonObj.append("]");

                System.out.println(jsonObj);

                resp.setStatus(HttpServletResponse.SC_OK);
                out.write(String.valueOf(jsonObj));
                System.out.println("Data loaded successfully");

            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Data not found");
                System.out.println("Data not found");
            }
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
}
