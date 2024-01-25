package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.ProfileGET;
import org.ucsc.enmoskill.model.Package;
import org.ucsc.enmoskill.model.ProfileModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static org.ucsc.enmoskill.Services.PackageService.getPackage;

public class PackageViewController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        //get packageid from request
        int packageId = Integer.parseInt(req.getParameter("packageId"));

        Package pkgObj = getPackage(packageId);

        int designerId = pkgObj.getDesignerUserId();
        System.out.println(designerId);

        req.setAttribute("designerId", designerId);
        req.setAttribute("role", "Designer");


// Now create the new request with the updated attributes
//        esignerRoleHttpServletRequest newReq = new DesignerRoleHttpServletRequest(req, String.valueOf(designerId), "Designer");

// The DesignerRoleHttpServletRequest class should retrieve these attributes in its constructor
// and set them accordingly.

//        ProfileModel profile = new ProfileModel(newReq);
//
//        if(profile.CheckReqiredFields()){
//            ProfileGET servise = new ProfileGET(profile,resp);
//            try {
//                servise.Run();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        }else{
//            resp.getWriter().write("User Id is Required!");
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        }
    }
}
