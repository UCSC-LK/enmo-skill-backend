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
        String key =req.getParameter("key");
        if (key.length()<12){
            resp.getWriter().write("This Is not a Valid Link");
        }else {
            ValidateUserPOST validateUserPOST =new ValidateUserPOST(resp,key);
            boolean a = validateUserPOST.genarate("1");
        }
    }
}
