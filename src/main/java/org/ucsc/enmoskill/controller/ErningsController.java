package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.ErningsGET;
import org.ucsc.enmoskill.Services.ErningsPUT;
import org.ucsc.enmoskill.model.ErningsModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

public class ErningsController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValid(token)) {
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            if (tokenInfo.getUserId() != null && tokenInfo.getRole() != null ){

                ErningsGET erningsGET = new ErningsGET(tokenInfo);
                ResponsModel responsModel=null;
                String price=null;
                if(req.getParameter("price")!=null){
                    price= req.getParameter("price");
                }
                try {
                    responsModel = erningsGET.Run(price);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                resp.getWriter().write(responsModel.getResMassage());
                resp.setStatus(responsModel.getResStatus());

            }else{
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("Please login");
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Please login");
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException {

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValid(token)) {

            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
            try (BufferedReader reader = req.getReader()) {
                ErningsModel erningsModel = new Gson().fromJson(reader, ErningsModel.class);
                if (erningsModel.getAmount()!=0){
                    ErningsPUT service = new ErningsPUT(erningsModel,tokenInfo);

                    ResponsModel responsModel = service.Run(erningsModel.getAmount());
                    resp.getWriter().write(responsModel.getResMassage());
                    resp.setStatus(responsModel.getResStatus());
                }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Missing withdrawal amount");
            }
            } catch (Exception e) {
                resp.getWriter().write(e.toString());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Please login");
        }
    }

}
