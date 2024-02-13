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

        try(BufferedReader reader = req.getReader()) {
            System.out.println(reader);
//            String json = reader.lines().collect(Collectors.joining());
//            System.out.println(json);
            ProfileModel profileModel = new Gson().fromJson(reader, ProfileModel.class);


            System.out.println("ssssssssssss");
            if(profileModel.getUserId() != 0 && profileModel.getRole() != null && profileModel.getFname() != null && profileModel.getLname() != null && profileModel.getDisplay_name() != null && profileModel.getDescription() != null ){
                ProfilePOST service = new ProfilePOST(profileModel,res);

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
    }

    protected void doPut(HttpServletRequest req,HttpServletResponse res){
        try(BufferedReader reader = req.getReader()) {
            ProfileModel profileModel = new Gson().fromJson(reader, ProfileModel.class);

            if(profileModel.getUserId() != 0 && profileModel.getRole() != null && profileModel.getFname() != null && profileModel.getLname() != null && profileModel.getDisplay_name() != null && profileModel.getDescription() != null ){
                ProfilePUT service = new ProfilePUT(profileModel,res);

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
    }


    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, IOException {
        resp.setContentType("application/json");

        ProfileModel profile = new ProfileModel(req);

        if(profile.CheckReqiredFields()){
            ProfileGET servise = new ProfileGET(profile,resp);
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
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        ProfileModel profile = new ProfileModel(req);

        Connection connection = DatabaseConnection.initializeDatabase();

        if (connection == null) {
            resp.getWriter().write("SQL Connection Error");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        if(profile.getUserId()!=0){

            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT t.url FROM enmo_database.users t where userID = "+profile.getUserId())) {
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


        }


    else{
            resp.getWriter().write("User Id is Required!");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
