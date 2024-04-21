package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.WarningService;
import org.ucsc.enmoskill.model.WarningModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class WarningController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
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
                WarningModel newWarning = gson.fromJson(reader, WarningModel.class);

                // insert new warning
                WarningService service = new WarningService();
                int result = service.insertWarning(newWarning);

                if (result > 0){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    out.write("Data inserted successfully");
                    System.out.println("Data inserted successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.write("Data didn't insert");
                    System.out.println("Data didn't insert");
                }


            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        }else if (tokenService.isTokenValidState(token) == 2) {
        resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
    } else {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }
    }
}
