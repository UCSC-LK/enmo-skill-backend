package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

        if (tokenService.isTokenValid(token)){
            if (tokenInfo.isDesigner()){
                // get dashboard data
                DesignerDashboardService service = new DesignerDashboardService();
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
                        out.write("Database error");
                        System.out.println("SQL Error: " + e.getMessage());
                        e.printStackTrace();
                        return;
                    }

                    NotificationGET notificationService = new NotificationGET(resp,tokenInfo);
                    ResponsModel responsModel = notificationService.Run();
//                    System.out.println(responsModel.getResMassage());
                    JsonObject jsonObject = new Gson().fromJson(responsModel.getResMassage(), JsonObject.class);

                    JsonArray notificationsArray = jsonObject.getAsJsonArray("notifications");

                    List<NotificationModel> notificationsList = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

                    for (int i = 0; i < notificationsArray.size(); i++) {
                        JsonObject notificationObject = notificationsArray.get(i).getAsJsonObject();

                        int notificationID = notificationObject.get("notificationID").getAsInt();
                        int userId = notificationObject.get("userid").getAsInt();
                        String content = notificationObject.get("content").getAsString();
                        String type = notificationObject.get("type").getAsString();
                        int status = notificationObject.get("status").getAsInt();
//                        Date date = Date.valueOf(notificationObject.get("date").getAsString());
                        Date date = null;
                        try {
                            date = new Date(sdf.parse(notificationObject.get("date").getAsString()).getTime());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        NotificationModel notification = new NotificationModel(notificationID, userId, content, type, status, date);
                        notificationsList.add(notification);

                        dashboardModel.setNotifications(notificationsList);
                    }

//                    // Print the list of notifications
//                    for (NotificationModel notification : notificationsList) {
//                        System.out.println("Notification ID: " + notification.getNotificationID());
//                        System.out.println("User ID: " + notification.getUserid());
//                        System.out.println("Content: " + notification.getContent());
//                        System.out.println("Type: " + notification.getType());
//                        System.out.println("Status: " + notification.getStatus());
//                        System.out.println("Date: " + notification.getDate());
//                        System.out.println("-----------------------------");
//                    }


                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.write(gson.toJson(dashboardModel));
                    System.out.println("Data loaded successfully");


//                    resp.setStatus(HttpServletResponse.SC_OK);
//                    out.write(gson.toJson(dashboardModel));
//                    System.out.println("Data loaded successfully");

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
