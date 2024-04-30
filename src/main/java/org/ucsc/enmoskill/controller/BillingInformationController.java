package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.Services.BillingInformationGET;
import org.ucsc.enmoskill.Services.BillingInformationPOST;
import org.ucsc.enmoskill.Services.BillingInformationPUT;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BillingInformationModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingInformationController extends HttpServlet {
    private TokenService.TokenInfo tokenInfo;
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValidState(token)==1){
            tokenInfo = tokenService.getTokenInfo(token);

            if(tokenInfo.isClient()){
                BillingInformationGET service = new BillingInformationGET(tokenInfo);

                ResponsModel responsModel = null;
                try {
                    responsModel = service.Run();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                resp.getWriter().write(responsModel.getResMassage());
                resp.setStatus(responsModel.getResStatus());
            }else{
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }


        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValidState(token)==1){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);

            if(tokenInfo.isClient()){
                try (BufferedReader reader = req.getReader()){

                    BillingInformationModel billingInformationModel = new Gson().fromJson(reader, BillingInformationModel.class);

                    if(!billingInformationModel.getFname().isEmpty() && !billingInformationModel.getLname().isEmpty() && !billingInformationModel.getpNumber().isEmpty()&&
                            !billingInformationModel.getEmail().isEmpty() && !billingInformationModel.getAddress().isEmpty()
                            && !billingInformationModel.getCity().isEmpty() && !billingInformationModel.getCountry().isEmpty()){

                        BillingInformationPOST service = new BillingInformationPOST(billingInformationModel,tokenInfo);
                        ResponsModel responsModel = service.Run();
                        resp.getWriter().write(responsModel.getResMassage());
                        resp.setStatus(responsModel.getResStatus());

                    }else{
                        resp.getWriter().write("Required field missing");
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }

                }catch (Exception e) {
                    resp.getWriter().write(e.toString());
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }else{
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException {

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValidState(token)==1){

            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);

            if(tokenInfo.isClient()){
                try (BufferedReader reader = req.getReader()){
                    BillingInformationModel billingInformationModel = new Gson().fromJson(reader, BillingInformationModel.class);

                    if(!billingInformationModel.getFname().isEmpty() && !billingInformationModel.getLname().isEmpty() && !billingInformationModel.getpNumber().isEmpty()&&
                            !billingInformationModel.getEmail().isEmpty() && !billingInformationModel.getAddress().isEmpty()
                            && !billingInformationModel.getCity().isEmpty() && !billingInformationModel.getCountry().isEmpty()){

                        BillingInformationPUT service = new BillingInformationPUT(billingInformationModel,tokenInfo);
                        ResponsModel responsModel = service.Run();
                        resp.getWriter().write(responsModel.getResMassage());
                        resp.setStatus(responsModel.getResStatus());
                    }else{
                        resp.getWriter().write("Required field missing");
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }



                }catch (Exception e) {
                    resp.getWriter().write(e.toString());
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }else{
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);


        if (tokenService.isTokenValidState(token)==1){
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);

            if(tokenInfo.isClient()){
                Connection connection = DatabaseConnection.initializeDatabase();
                if(connection==null){

                    resp.getWriter().write("SQL Connection Error");
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

                }else{
                    if(req.getParameter("update")!=null){
                        String update =req.getParameter("update");

                        String query = "SELECT t.email,t.contact_no FROM enmo_database.users t WHERE t.userID = "+tokenInfo.getUserId();
                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement(query);

                            ResultSet result = preparedStatement.executeQuery();
                            JsonArray jsonArray = new JsonArray();
                            Gson gson = new Gson();


                            while (result.next()){
                                BillingInformationModel billingInformationModel = new BillingInformationModel(result,update);
                                JsonObject jsonObject = gson.toJsonTree(billingInformationModel).getAsJsonObject();
                                jsonArray.add(jsonObject);
                            }

                            resp.getWriter().write(jsonArray.toString());
                            resp.setStatus(HttpServletResponse.SC_OK);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }else{
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
