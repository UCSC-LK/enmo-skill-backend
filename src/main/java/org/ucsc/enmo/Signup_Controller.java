package org.ucsc.enmo;

import Classes.Signup_User;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class Signup_Controller extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);

//        String username = req.getParameter("username");
//        String password = req.getParameter("password");

        User user = new User(req);

        try {
            Signup_User.signup(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
