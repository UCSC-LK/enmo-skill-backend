package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.ucsc.enmoskill.Services.DesignerDashboardService;
import org.ucsc.enmoskill.Services.NotificationGET;
import org.ucsc.enmoskill.Services.ProfileGET;
import org.ucsc.enmoskill.model.DesignerDashboardModel;
import org.ucsc.enmoskill.model.NotificationModel;
import org.ucsc.enmoskill.model.ProfileModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

        int designerUserId = 0;
        try {
            designerUserId = Integer.parseInt(tokenInfo.getUserId());
            // rest of the code
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Invalid user ID");
            System.out.println("Invalid user ID: " + e.getMessage());
            return;
        }

        if (tokenService.isTokenValidState(token) == 1){
            if (tokenInfo.isDesigner()){

                // get dashboard data
                DesignerDashboardService service = new DesignerDashboardService();

                try {
                    DesignerDashboardModel dashboardModel = service.getData(designerUserId);

                    if (dashboardModel != null){

                        // get profile data
                        ProfileModel profile = new ProfileModel(dashboardModel.getDesignerId(), "Designer", null, null, null, null, null, null);

                        ProfileGET servise = new ProfileGET(profile,resp);
                        try{
                            StringBuilder profilejson = new StringBuilder();
                            ResponsModel responsModel= servise.Run();
                            profilejson.append(responsModel.getResMassage());

                            profile = gson.fromJson(String.valueOf(profilejson), ProfileModel.class);
                            dashboardModel.setProfileModel(profile);
//                            System.out.println(responsModel.getResMassage());
                        } catch (SQLException e) {
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            out.write(e.toString());

                        }

                        // get notifications
                        NotificationGET notificationService = new NotificationGET(resp,tokenInfo);

                        try{
                            ResponsModel responsModel = notificationService.Run();

                            // convert the notification string to a json object
                            JsonObject jsonObject = new Gson().fromJson(responsModel.getResMassage(), JsonObject.class);

                            // check whether the notifications array is empty
                            if (jsonObject.get("count").getAsInt() > 0){

                                // extract only the notifications array
                                JsonArray notificationsArray = jsonObject.getAsJsonArray("notifications");

                                List<NotificationModel> notificationsList = new ArrayList<>();

                                // iterate and covert each into an notificationmodel object
                                for (int i = 0; i < notificationsArray.size(); i++) {
                                    JsonObject notificationObject = notificationsArray.get(i).getAsJsonObject();
                                    NotificationModel notificationModel = gson.fromJson(notificationObject, NotificationModel.class);

                                    // append to list
                                    notificationsList.add(notificationModel);

                                    // add the notification list to the dashboard model
                                    dashboardModel.setNotifications(notificationsList);
                                }
                            } else {
                                dashboardModel.setNotifications(null);
                            }

                            resp.setStatus(HttpServletResponse.SC_OK);
                            out.write(gson.toJson(dashboardModel));
                            System.out.println("Data loaded successfully");


                        } catch (Exception e) {
                            out.write(e.toString());
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);;
                        }



                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("Data fetch unsuccessful");
                        System.out.println("Data fetch unsuccessful");
                    }
                } catch (IOException | JsonSyntaxException e) {
                    out.write(e.toString());
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }


            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("Authorization failed");
                System.out.println("Authorization failed");
            }
        }else if (tokenService.isTokenValidState(token) == 2) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }




    }
}
