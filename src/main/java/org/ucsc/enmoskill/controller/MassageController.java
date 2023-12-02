package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.BuyerRequestPOST;
import org.ucsc.enmoskill.Services.ChatsGET;
import org.ucsc.enmoskill.Services.MassagesGET;
import org.ucsc.enmoskill.Services.MessagesPOST;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.MessageModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

public class MassageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("userid")!=null&&req.getParameter("chatid")!=null){

            MassagesGET massagesGET = new MassagesGET(resp,req.getParameter("userid"),req.getParameter("chatid"));
            try {
                massagesGET.Run();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Required Fields Missing");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (BufferedReader reader = req.getReader()){
            MessageModel messageModel = new Gson().fromJson(reader, MessageModel.class);
            if (messageModel.getMessage()!=null&&messageModel.getUser()!=null&&messageModel.getChatid()!=null&&!messageModel.getMessage().equals("")){
                MessagesPOST messagesPOST= new  MessagesPOST(resp, messageModel);
                messagesPOST.Run();
            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Required Field Missing");
            }
        } catch (Exception e) {
            resp.getWriter().write(e.toString());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


    }
}
