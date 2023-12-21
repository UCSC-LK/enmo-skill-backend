package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.ProfileGET;
import org.ucsc.enmoskill.Services.ProfilePOST;
import org.ucsc.enmoskill.model.ProfileModel;
import org.ucsc.enmoskill.model.Req_BRlist;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
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
                service.Run();

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
                servise.Run();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            resp.getWriter().write("User Id is Required!");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
