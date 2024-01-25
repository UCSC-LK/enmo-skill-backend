package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.ChatsGET;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ChatsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("userid")!=null){

            ChatsGET chatsGET = new ChatsGET(resp,req.getParameter("userid"));
            try {
                chatsGET.Run();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Required Fields Missing");
        }
    }
}