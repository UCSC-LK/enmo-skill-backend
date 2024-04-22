package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.OrderPriceGET;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class OrderPriceController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);



        if(tokenService.isTokenValid(token)){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);

            String packegeID=null;
            if(req.getParameter("packegeID")!=null){
                packegeID= req.getParameter("packegeID");
            }
            OrderPriceGET service = new OrderPriceGET(tokenInfo);
            ResponsModel responsModel = null;
            try {
                responsModel = service.Run(packegeID);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            resp.getWriter().write(responsModel.getResMassage());
            resp.setStatus(responsModel.getResStatus());

        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Please login");
        }
    }
}
