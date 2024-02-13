package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.ProfileGET;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.model.ProfileModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static org.ucsc.enmoskill.Services.PackageService.getPackage;
import static org.ucsc.enmoskill.Services.PricePackageService.fetchData;

public class PackageViewController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        if (tokenService.isTokenValid(token)){
            Gson gson = new Gson();

            StringBuilder jsonObj = new StringBuilder("[");


            //get packageid from request
            int packageId = Integer.parseInt(req.getParameter("packageId"));

            // get package details
            Package pkgObj = getPackage(packageId);

            if (pkgObj == null){

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Package not found");
                System.out.println("Package not found");

            } else {
                int designerId = pkgObj.getDesignerUserId();

                String packageData = gson.toJson(pkgObj);
                StringBuilder pkgJson = new StringBuilder(packageData);
                jsonObj.append(pkgJson);
                jsonObj.append(",");

                ProfileModel profile = new ProfileModel(designerId, "Designer", null, null, null, null, null, null);

                if(profile.CheckReqiredFields()){
                    ProfileGET servise = new ProfileGET(profile,resp);
                    try {
                        ResponsModel responsModel= servise.Run();
                        jsonObj.append(responsModel.getResMassage());
                        jsonObj.append(",");


//                System.out.println(responsModel.getResMassage());
//                resp.getWriter().write(responsModel.getResMassage());
//                resp.getWriter().write(packageJson);
//                resp.setStatus(responsModel.getResStatus());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    out.write("User Id is Required!");
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

                // fetch package prices
                int category = pkgObj.getCategory();
                System.out.println("Category is :"+category);

                StringBuilder sb = fetchData(packageId, category);

                if (sb == null){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Data not found");
                    System.out.println("Data not found");
                } else {
//                resp.setStatus(HttpServletResponse.SC_OK);
                    jsonObj.append("{");
                    jsonObj.insert(jsonObj.length(), "\"pricing\":" + sb);

//                jsonObj.append(sb);
                    jsonObj.append("}");
//                out.write(String.valueOf(sb));
//                System.out.println("Data loaded successfully");
                }

                jsonObj.append("]");

                resp.setStatus(HttpServletResponse.SC_OK);
                out.write(String.valueOf(jsonObj));
                System.out.println("Data loaded successfully");

            }






        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }
    }



}
