package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ucsc.enmoskill.Services.ProfileGET;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.model.ProfileModel;
import org.ucsc.enmoskill.model.ResponsModel;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import static org.ucsc.enmoskill.Services.PackageListService.*;
import static org.ucsc.enmoskill.Services.PricePackageService.*;

public class PackageListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        int category = Integer.parseInt(req.getParameter("category"));
        int priceCode = Integer.parseInt(req.getParameter("price"));
        int delTimeCode = Integer.parseInt(req.getParameter("delTimeCode"));
        int language = Integer.parseInt(req.getParameter("language"));

        System.out.println(priceCode);

        List<Package> packageList = null;

        if (category>4 || category<0){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Data not found");
            System.out.println("Data not found");
        } else {
            StringBuilder jsonObj = new StringBuilder("[");

            Gson gson = new Gson();

            if (priceCode == 0 && delTimeCode == 0 && language == 0){
                packageList = getAllPackages(category);
            } else if (priceCode == 1 && delTimeCode == 0 && language == 0) {
                packageList = geLowPackages(category);
            } else if (priceCode == 2 && delTimeCode == 0 && language == 0){
                packageList = getMidPackages(category);
            } else if (priceCode == 3 && delTimeCode == 0 && language == 0){
                packageList = getHighPackages(category);
            } else if ((priceCode!=0 && priceCode!=1 && priceCode!=2 && priceCode!=3) && delTimeCode == 0 && language == 0){
                packageList = getCustomPricePackages(category, priceCode);
            } else if (priceCode == 0 && (delTimeCode == 1 || delTimeCode == 3 || delTimeCode == 7) && language == 0) {
                packageList = getPkgesByDuration(category, delTimeCode);
            } else if (priceCode == 0 && delTimeCode == 0 && (language == 1 || language == 2 || language == 3)) {
                packageList = getPkgesByLang(category, language);
            } else if (priceCode == 1 && (delTimeCode == 1 || delTimeCode == 3 || delTimeCode == 7) && language == 0) {
                packageList = getPkgesLowDel(category, delTimeCode);
            } else if (priceCode == 1 && delTimeCode == 0 && (language == 1 || language == 2 || language == 3)) {
                packageList = getPkgesLowLang(category, language);
            } else if (priceCode == 1 && (delTimeCode == 1 || delTimeCode == 3 || delTimeCode == 7) && (language == 1 || language == 2 || language == 3)) {
                packageList = getPkgesLowDelLang(category, delTimeCode, language);
            } else if (priceCode == 2 && (delTimeCode == 1 || delTimeCode == 3 || delTimeCode == 7) && language == 0) {
                packageList = getPkgesMidDel(category, delTimeCode);
            } else if (priceCode == 2 && delTimeCode == 0 && (language == 1 || language == 2 || language == 3)) {
                packageList = getPkgesMidLang(category, language);
            } else if (priceCode == 2 && (delTimeCode == 1 || delTimeCode == 3 || delTimeCode == 7) && (language == 1 || language == 2 || language == 3)) {
                packageList = getPkgesMidDelLang(category, delTimeCode, language);
            } else if (priceCode == 3 && (delTimeCode == 1 || delTimeCode == 3 || delTimeCode == 7) && language == 0) {
                packageList = getPkgesHighDel(category, delTimeCode);
            } else if (priceCode == 3 && delTimeCode == 0 && (language == 1 || language == 2 || language == 3)) {
                packageList = getPkgesHighLang(category, language);
            } else if (priceCode == 3 && (delTimeCode == 1 || delTimeCode == 3 || delTimeCode == 7) && (language == 1 || language == 2 || language == 3)) {
                packageList = getPkgesHighDelLang(category, delTimeCode, language);
            } else if ((priceCode!=0 && priceCode!=1 && priceCode!=2 && priceCode!=3) && (delTimeCode == 1 || delTimeCode == 3 || delTimeCode == 7) && language == 0){
                packageList = getCustPriceDel(category, priceCode, delTimeCode);
            } else if ((priceCode!=0 && priceCode!=1 && priceCode!=2 && priceCode!=3) && delTimeCode == 0 && (language == 1 || language == 2 || language == 3)){
                packageList = getCustPriceLang(category, priceCode, delTimeCode);
            } else if ((priceCode!=0 && priceCode!=1 && priceCode!=2 && priceCode!=3) && (delTimeCode == 1 || delTimeCode == 3 || delTimeCode == 7) && (language == 1 || language == 2 || language == 3)){
                packageList = getCustPriceDelLang(category, priceCode, delTimeCode, language);
            }


//            List<Package> packageList = getAllPackages(category);
            if (!packageList.isEmpty()){
                for (Package newpackage:packageList) {
                    int packageId = newpackage.getPackageId();

                    float lowestPrice = getBronzePrice(packageId);
                    float highestPrice = getPlatinumPrice(packageId);
                    String designerName = "";

                    ProfileModel profile = new ProfileModel(newpackage.getDesignerUserId(), "Designer", null, null, null, null, null, null);
                    if (profile.CheckReqiredFields()){
                        ProfileGET servise = new ProfileGET(profile,resp);
                        try{
                            StringBuilder profilejson = new StringBuilder();
                            ResponsModel responsModel= servise.Run();
                            profilejson.append(responsModel.getResMassage());

                            // Parse the JSON string
                            JsonParser parser = new JsonParser();
                            JsonObject jsonObject = parser.parse(String.valueOf(profilejson)).getAsJsonObject();

                            // Get the value of the display_name property
                            designerName = jsonObject.get("display_name").getAsString();



                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    String pkg = gson.toJson(newpackage);
                    StringBuilder aPackage = new StringBuilder(pkg);

                    aPackage.insert(aPackage.length()-1, ", \"starterPrice\":"+lowestPrice);
                    aPackage.insert(aPackage.length()-1, ", \"highestPrice\":"+highestPrice);
                    aPackage.insert(aPackage.length()-1, ", \"designerName\":\""+designerName+"\"");


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