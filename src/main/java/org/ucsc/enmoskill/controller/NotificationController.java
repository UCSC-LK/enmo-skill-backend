package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.BuyerRequestGET;
import org.ucsc.enmoskill.Services.NotificationGET;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValidState(token)==1){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            NotificationGET service = new NotificationGET(resp,tokenInfo);
            ResponsModel responsModel = service.Run();
            resp.setStatus(responsModel.getResStatus());
            resp.getWriter().write(responsModel.getResMassage());
        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    protected void doPut (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValidState(token)==1){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            String userId = tokenInfo.getUserId();
            System.out.println(userId);
            String notificationId = req.getParameter("notificationId");
            String query = "UPDATE enmo_database.notifications SET status = 0 WHERE notificationId = ? AND userId = ?";
            try {
                PreparedStatement preparedStatement = DatabaseConnection.initializeDatabase().prepareStatement(query);
                preparedStatement.setString(1,notificationId);
                preparedStatement.setString(2,userId);
                int Executed = preparedStatement.executeUpdate();
                if (Executed>0){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().flush();
                }else{
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    resp.getWriter().write("Notification not found or not owned by the user");
                    resp.getWriter().flush();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
