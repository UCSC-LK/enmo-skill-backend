package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.utils.ForgetPassword;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ForgetPasswordController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        ForgetPassword forgetPassword = new ForgetPassword();
        if(forgetPassword.isEmailExists(email)){
            String token = forgetPassword.generateToken(email);
            if(forgetPassword.sendEmail(email, token)){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().flush();
            }else {
                resp.setStatus(HttpServletResponse.SC_CONTINUE);
                resp.getWriter().flush();
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().flush();
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        String password = req.getParameter("password");
        ForgetPassword forgetPassword = new ForgetPassword();
        if(forgetPassword.isValidToken(token)){
            String email = forgetPassword.getInfo(token);
            if(forgetPassword.updatePassword(email, password)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().flush();
            }else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().flush();
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().flush();
        }
    }
}
