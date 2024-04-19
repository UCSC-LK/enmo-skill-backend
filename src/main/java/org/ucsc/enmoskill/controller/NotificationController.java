package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.BuyerRequestGET;
import org.ucsc.enmoskill.Services.NotificationGET;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NotificationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValidState(token)==1){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            NotificationGET service = new NotificationGET(resp,tokenInfo);
            service.Run();
        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
