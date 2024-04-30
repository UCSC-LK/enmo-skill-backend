package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.ucsc.enmoskill.Services.PackageViewModelService;
import org.ucsc.enmoskill.Services.PricePackageService;
import org.ucsc.enmoskill.Services.ProfileGET;
import org.ucsc.enmoskill.model.*;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.ucsc.enmoskill.Services.PackageService.getPackage;

public class PackageViewController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get called");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);


        try {

            tokenInfo = tokenService.getTokenInfo(token);

            if (tokenService.isTokenValidState(token) == 1){

                try {
                    int packageId = Integer.parseInt(req.getParameter("packageId"));

                    PackageViewModel viewModel = new PackageViewModel();
                    PackageViewModelService service1 = new PackageViewModelService();

                    service1.updateClickCount(packageId);
                    // fetch package data
                    Package pkgObj = getPackage(packageId);

                    if (pkgObj == null){
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("Package not found");
                        System.out.println("Package not found");
                    } else {

                        viewModel.setPackageModel(pkgObj);

                        // fetch profile data
                        ProfileModel profile = new ProfileModel(pkgObj.getDesignerUserId(), "Designer", null, null, null, null, null, null, null);
                        System.out.println(gson.toJson(profile));
                        if (pkgObj.getDesignerUserId() != 0){
                            if (profile.CheckReqiredFields()){
                                ProfileGET servise = new ProfileGET(profile,resp);
                                try{
                                    StringBuilder profilejson = new StringBuilder();
                                    ResponsModel responsModel= servise.Run();
                                    profilejson.append(responsModel.getResMassage());

                                    profile = gson.fromJson(String.valueOf(profilejson), ProfileModel.class);
                                    viewModel.setProfileModel(profile);

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                // fetch package pricing data
                                List<PackagePricing> priceList = new ArrayList<>();
                                PricePackageService service = new PricePackageService();
                                priceList = service.fetchData(packageId);

                                if (priceList != null){
                                    viewModel.setPricings(priceList);

                                    PackageViewModel viewModel1 = service1.getData(packageId, pkgObj.getDesignerUserId());

                                    if (viewModel1 != null){

                                        viewModel.setPendingOrders(viewModel1.getPendingOrders());
                                        viewModel.setUserRatings(viewModel1.getUserRatings());

                                        System.out.println(gson.toJson(viewModel));

                                        resp.setStatus(HttpServletResponse.SC_OK);
                                        out.write(gson.toJson(viewModel));

                                    } else {
                                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                        out.write("Cannot review data");
                                        System.out.println("Cannot get data");
                                    }

                                } else {
                                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                    out.write("Cannot get data");
                                    System.out.println("Cannot get data");
                                }

                            }
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
        }catch (ExpiredJwtException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


    }



}
