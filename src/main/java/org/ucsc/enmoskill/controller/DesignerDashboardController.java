package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.DesignerDashboardService;
import org.ucsc.enmoskill.Services.ProfileGET;
import org.ucsc.enmoskill.model.DesignerDashboardModel;
import org.ucsc.enmoskill.model.ProfileModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class DesignerDashboardController extends HttpServlet {
    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        Gson gson = new Gson();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//default req is a request of controller

        tokenInfo = tokenService.getTokenInfo(token);

        int designerUserId = Integer.parseInt(tokenInfo.getUserId());

        if (tokenService.isTokenValid(token)){
            if (tokenInfo.isDesigner()){
                // get dashboard data
                DesignerDashboardService service = new DesignerDashboardService();
                DesignerDashboardModel dashboardModel = service.getData(designerUserId);

                if (dashboardModel != null){

                    // get profile data
                    ProfileModel profile = new ProfileModel(dashboardModel.getDesignerId(), "Designer", null, null, null, null, null, null);

                    if (profile.CheckReqiredFields()){
                        ProfileGET servise = new ProfileGET(profile,resp);
                        try{
                            StringBuilder profilejson = new StringBuilder();
                            ResponsModel responsModel= servise.Run();
                            profilejson.append(responsModel.getResMassage());

                            profile = gson.fromJson(String.valueOf(profilejson), ProfileModel.class);
                            dashboardModel.setProfileModel(profile);

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.write(gson.toJson(dashboardModel));
                        System.out.println("Data loaded successfully");
                    }

                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Data fetch unsuccessful");
                    System.out.println("Data fetch unsuccessful");
                }






            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("Authorization failed");
                System.out.println("Authorization failed");
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }




    }
}
