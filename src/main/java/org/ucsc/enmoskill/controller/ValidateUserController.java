package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.ValidateUserPOST;

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
                resp.getWriter().write("This Is not a Valid Link");
            }else {
                ValidateUserPOST validateUserPOST =new ValidateUserPOST(resp,key);

                if(!validateUserPOST.Validate()){

                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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

}
