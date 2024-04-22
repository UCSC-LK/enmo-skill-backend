package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.BannService;
import org.ucsc.enmoskill.Services.WarningService;
import org.ucsc.enmoskill.model.BannModel;
import org.ucsc.enmoskill.model.WarningModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class BannController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//defulat req is a request of controller

        tokenInfo = tokenService.getTokenInfo(token);

        if (tokenService.isTokenValidState(token) == 1){

            if (tokenInfo.isAdmin()){

                // extract request body
                BufferedReader reader = req.getReader();
                BannModel bann = gson.fromJson(reader, BannModel.class);

                // insert new warning
                BannService service = new BannService();
                int result = service.insertBann(bann);

                if (result > 0){

                    service.send(bann.getUserId(), resp);
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.write("Problem occurred while suspending the account");
                    System.out.println("Problem occurred while suspending the account");
                }


            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        }else if (tokenService.isTokenValidState(token) == 2) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }
    }


}
