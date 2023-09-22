package org.ucsc.enmo;



import Classes.DatabaseConnectionManager;
import Classes.Validate_user;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User_Controler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OutputStream outputStream = resp.getOutputStream();
        BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write("hello  enmoskill");
        bufferedWriter.flush();
        bufferedWriter.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req);
//        Validate_user.Validate(user);
        if(Validate_user.Validate(user)){
//            System.out.println("abc");
            resp.setStatus(200);
            resp.getWriter().write("User Signed In!");
        }else {
//            System.out.println("xyz");
            resp.getWriter().write("User not Signed In!");
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}