package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.Services.ProfileGET;
import org.ucsc.enmoskill.Services.ProfilePOST;
import org.ucsc.enmoskill.Services.ProfilePUT;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ProfileModel;
import org.ucsc.enmoskill.model.Req_BRlist;
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

public class ProfileController extends HttpServlet {

    protected void doPost(HttpServletRequest req,HttpServletResponse res){

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValid(token)){

            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);

            try(BufferedReader reader = req.getReader()) {

//            String json = reader.lines().collect(Collectors.joining());
//            System.out.println(json);
                ProfileModel profileModel = new Gson().fromJson(reader, ProfileModel.class);

                if(profileModel.getFname() != null && profileModel.getLname() != null && profileModel.getDisplay_name() != null && profileModel.getDescription() != null ){
                    ProfilePOST service = new ProfilePOST(profileModel,tokenInfo);

                    ResponsModel responsModel= service.Run();
                    res.getWriter().write(responsModel.getResMassage());
                    res.setStatus(responsModel.getResStatus());

                }else {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.getWriter().write("Required Field Missing");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);

            }
        }else{
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    protected void doPut(HttpServletRequest req,HttpServletResponse res){

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValid(token)){

            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);

            try(BufferedReader reader = req.getReader()) {
                ProfileModel profileModel = new Gson().fromJson(reader, ProfileModel.class);

                if(profileModel.getFname() != null && profileModel.getLname() != null && profileModel.getDisplay_name() != null && profileModel.getDescription() != null ){
                    ProfilePUT service = new ProfilePUT(profileModel,tokenInfo);

                    ResponsModel responsModel= service.Run();
                    res.getWriter().write(responsModel.getResMassage());
                    res.setStatus(responsModel.getResStatus());

                }else {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.getWriter().write("Required Field Missing");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }else{
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, IOException {
        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);
        System.out.println(token);

        if(tokenService.isTokenValid(token)){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);

            if(tokenInfo.getRole() != null && tokenInfo.getUserId() != null){
                ProfileGET servise = new ProfileGET(tokenInfo);

                try {
                    ResponsModel responsModel= servise.Run();
                    resp.getWriter().write(responsModel.getResMassage());
                    resp.setStatus(responsModel.getResStatus());

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                resp.getWriter().write("User Id is Required!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValid(token)){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);

            Connection connection = DatabaseConnection.initializeDatabase();

            if (connection == null) {
                resp.getWriter().write("SQL Connection Error");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            if(tokenInfo.getUserId()!=null){

                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT t.url FROM enmo_database.users t where userID = "+tokenInfo.getUserId())) {
//                preparedStatement.setInt(1, profileModel.getUserId());

                    ResultSet resultSet = preparedStatement.executeQuery();

                    JsonObject jsonObject = new JsonObject();

                    if (resultSet.next()) {
                        String url = resultSet.getString("url");
                        jsonObject.addProperty("url",url);
                    }


                    resp.getWriter().write(jsonObject.toString());
//                System.out.println(resp);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                resp.getWriter().write("User Id is Required!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


}
