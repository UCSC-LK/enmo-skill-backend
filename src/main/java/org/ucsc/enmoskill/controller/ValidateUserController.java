package org.ucsc.enmoskill.controller;

import com.google.gson.JsonObject;
import org.ucsc.enmoskill.Services.ValidateUserPOST;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ValidateUserController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getParameter("key")!=null){
            String key =req.getParameter("key");
            if (key.length()<15){
                resp.setStatus(HttpServletResponse.SC_GONE);
                resp.getWriter().write("This Is not a Valid Link");
            }else {
                ValidateUserPOST validateUserPOST =new ValidateUserPOST(resp,key);

                if(!validateUserPOST.Validate()){

                    resp.setStatus(HttpServletResponse.SC_GONE);
                    resp.getWriter().write("Critical Issue With Your Link. Reach Contact Support.");
                }
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Bad Request");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("option")!=null&&req.getParameter("email")!=null){
            String option =req.getParameter("option");
            if (option.equals("send")){
                ValidateUserPOST validateUserPOST =new ValidateUserPOST(resp);
                validateUserPOST.send(req.getParameter("email"));
            } else if (option.equals("resend")) {

            }
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Bad Request");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (req.getParameter("check")==null){
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
            String Status = tokenService.isTokenValid(token) ? "Valid":"Invalid";
            String User = tokenInfo.getUserId();
            String Role = tokenInfo.getRole();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Status",Status);
            jsonObject.addProperty("User",User);
            jsonObject.addProperty("Role",Role);
            resp.getWriter().write(jsonObject.toString());
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);

        } else if(req.getParameter("check").equals("true")){

            if(tokenService.isTokenValid(token)){
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            }else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }


        } else if (req.getParameter("check").equals("false")) {

            if(tokenService.isTokenValid(token)){

                TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
                String new_token= tokenService.generateToken(tokenInfo.getUserId(),tokenInfo.getRole());
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("JWT",new_token);
                System.out.println("new_token: "+jsonObject);
                resp.getWriter().write(jsonObject.toString());
                resp.setStatus(HttpServletResponse.SC_ACCEPTED);


            }else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }


    }
}
