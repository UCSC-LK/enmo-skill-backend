package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.ucsc.enmoskill.Services.AdminDashboardService;
import org.ucsc.enmoskill.model.AdminDashboardModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminDashboradController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        try {
            // get token data
            tokenInfo = tokenService.getTokenInfo(token);

            // check validy of token
            if(tokenService.isTokenValidState(token) == 1) {

                // check if the user is an admin
                if (tokenInfo.isAdmin()) {


                    // get data
                    AdminDashboardService service = new AdminDashboardService();

                    AdminDashboardModel dashboardModel = new AdminDashboardModel();
                    dashboardModel = service.getDashboardData();
                    dashboardModel.setUserCount(service.getUserCount()); // set user count
                    dashboardModel.setPackageCount(service.getPackageCount()); // set package count
                    dashboardModel.setTotalEarnings(service.getTotalEarnings()); // set total earnings
                    dashboardModel.setDate_orders(service.getOrderData());

                    if (dashboardModel != null){
                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.write(gson.toJson(dashboardModel));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("Internal server error");
                    }

//                    System.out.println(gson.toJson(dashboardModel));

//                    resp.setStatus(HttpServletResponse.SC_OK);
//                    resp.setContentType("application/json");
//                    String successMessage = "{\"message\": \"Admin Dashboard\"}";
//                    resp.getWriter().write(successMessage);
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().write("You are not an admin");
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
