package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ucsc.enmoskill.Services.PackageListService;
import org.ucsc.enmoskill.Services.ProfileGET;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.model.PackageBlockModel;
import org.ucsc.enmoskill.model.ProfileModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;
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

    private TokenService.TokenInfo tokenInfo;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);


        int category = Integer.parseInt(req.getParameter("category"));
        int priceCode = Integer.parseInt(req.getParameter("price"));
        int delTimeCode = Integer.parseInt(req.getParameter("delTimeCode"));
        int language = Integer.parseInt(req.getParameter("language"));
        float reviews = Float.parseFloat(req.getParameter("reviewCode"));

        if (tokenService.isTokenValidState(token) == 1){

            List<PackageBlockModel> packageList1 = null;
            List<PackageBlockModel> packageList2 = null;

            try {
                if (category>4 || category<0){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Data not found");
                    System.out.println("Data not found");
                } else {
                    Gson gson = new Gson();

                    PackageListService service = new PackageListService();
                    int packageCount;

                    do {
                        // fetch all the required details to filter and display
                        packageList1 = service.getPackages();
//                    System.out.println(gson.toJson(packageList1));

                        // fetch packages count
                        packageCount = service.countPackages();
                        System.out.println(packageCount);
                        System.out.println("list: " + packageList1.size());

                    } while (packageCount != packageList1.size());

                    packageList2 = service.filterPackages(category, priceCode, delTimeCode, language, reviews, packageList1);

                    if (!packageList2.isEmpty()){

                        String json = gson.toJson(packageList2);
                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.write(json);
                        System.out.println("Data loaded successfully");

                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("Cannot get data");
                        System.out.println("Cannot get data");
                    }

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



    }

}