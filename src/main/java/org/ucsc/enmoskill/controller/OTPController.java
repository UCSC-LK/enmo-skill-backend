package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.Services.OtpGET;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.otpModel;
import org.ucsc.enmoskill.utils.OTPhash;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class OTPController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");

        String phoneNumber=null;
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValidState(token)==1){
            if(req.getParameter("phoneNumber")!=null){
                phoneNumber=req.getParameter("phoneNumber");
            }else{
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("Phone number required");
            }
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);

            OtpGET service = new OtpGET(tokenInfo);

            ResponsModel responsModel=service.Run(phoneNumber);

            res.getWriter().write(responsModel.getResMassage());
            res.setStatus(responsModel.getResStatus());
        }else if(tokenService.isTokenValidState(token)==2){
            res.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValidState(token)==1){

            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            try (BufferedReader reader = req.getReader()){
                otpModel otpmodel = new Gson().fromJson(reader,otpModel.class);

                Dotenv dotenv = Dotenv.load();
                String signKey = dotenv.get("otpSign");

                OTPhash otphash = new OTPhash();
                boolean result = otphash.checkOTP(signKey+otpmodel.getOtp(),otpmodel.getOtpHash());

                System.out.println(result);

            } catch (Exception e) {
            resp.getWriter().write(e.toString());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
