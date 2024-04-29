package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;

import org.ucsc.enmoskill.Services.BuyerRequestDELETE;
import org.ucsc.enmoskill.Services.BuyerRequestGET;
import org.ucsc.enmoskill.Services.BuyerRequestPOST;
import org.ucsc.enmoskill.Services.BuyerRequestPUT;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.Req_BRlist;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;


public class Buyer_request_Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {


        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValidState(token)==1){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            BuyerRequestGET service = new BuyerRequestGET(resp,tokenInfo);
            service.Run();
        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {

        resp.setContentType("application/json");
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);
        if(tokenService.isTokenValid(token)){

            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            try (BufferedReader reader = req.getReader()){
                BuyerRequestModel buyerRequestModel = new Gson().fromJson(reader, BuyerRequestModel.class);
                if (buyerRequestModel.getTitle()!=null&&buyerRequestModel.getDiscription()!=null&&buyerRequestModel.getDuration()>0&&buyerRequestModel.getBudget()>0){
                    BuyerRequestPOST service = new BuyerRequestPOST(resp,buyerRequestModel,tokenInfo);
                    service.Run();
                }else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Required Field Missing or Invalid Data");
                }
            } catch (Exception e) {
                resp.getWriter().write(e.toString());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String reqesttid= req.getParameter("requestID");
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);
        TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
        if(tokenService.isTokenValid(token)){
            if(reqesttid!=null){
                try {
                    new BuyerRequestDELETE(reqesttid,resp,tokenInfo);
                } catch (SQLException e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    throw new RuntimeException(e);
                }
            }
            else {
                resp.getWriter().write("Invalid Parameter");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        resp.setContentType("application/json");
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValid(token)){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            try (BufferedReader reader = req.getReader()){
                BuyerRequestModel buyerRequestModel = new Gson().fromJson(reader, BuyerRequestModel.class);
                buyerRequestModel.setUserID(Integer.parseInt(tokenInfo.getUserId()));
                if (buyerRequestModel.getTitle()!=null&&buyerRequestModel.getDiscription()!=null&&buyerRequestModel.getDuration()>0&&buyerRequestModel.getRequestID()!=0&&buyerRequestModel.getBudget()>0){
                    BuyerRequestPUT service = new BuyerRequestPUT(resp,buyerRequestModel);
                    service.Run();
                }else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Required Field Missing or Invalid Data");
                }
            } catch (Exception e) {
                resp.getWriter().write(e.toString());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
