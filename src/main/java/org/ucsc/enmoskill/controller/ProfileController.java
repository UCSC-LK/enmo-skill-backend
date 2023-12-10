package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.DesignerProfile;
import org.ucsc.enmoskill.model.Profile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ProfileController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Profile profile = new Profile(req);

        if(profile.CheckReqiredFields()){
            DesignerProfile servise = new DesignerProfile(profile,resp);
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
