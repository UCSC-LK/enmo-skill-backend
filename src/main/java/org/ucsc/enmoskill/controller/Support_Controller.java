package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.*;
import org.ucsc.enmoskill.model.Req_BRlist;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

public class Support_Controller extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (BufferedReader reader = req.getReader()){

            SupprtModel supportmodel = new Gson().fromJson(reader, SupprtModel.class);

            if (supportmodel.getRequesterID()!=0&&supportmodel.getDescription()!=null&&supportmodel.getSubject()!=null){

                SupportPOST service = new SupportPOST(supportmodel);

//                service.Run();
                ResponsModel responsModel = service.Run();
                resp.getWriter().write(responsModel.getResMassage());
                resp.setStatus(responsModel.getResStatus());

            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Required Field Missing");
            }
        } catch (Exception e) {
            resp.getWriter().write(e.toString());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (BufferedReader reader = req.getReader()){

            SupprtModel supportmodel = new Gson().fromJson(reader, SupprtModel.class);

            if (supportmodel.getRef_no()!=0&&supportmodel.getDescription()!=null&&supportmodel.getSubject()!=null){

                SupportPUT service = new SupportPUT(supportmodel);

//                service.Run();
                ResponsModel responsModel = service.Run();
                resp.getWriter().write(responsModel.getResMassage());
                resp.setStatus(responsModel.getResStatus());
            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Required Field Missing");
            }
        } catch (Exception e) {
            resp.getWriter().write(e.toString());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String TicketID= req.getParameter("TicketID");
        if(TicketID!=null){
            try {
                new SupportDELETE(TicketID,resp);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }
        }
        else {
            resp.getWriter().write("Ticket ID required");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String popup=null;
        if(req.getParameter("popup")!=null){
            popup= req.getParameter("popup");
        }


        Req_BRlist reqBRlist =new Req_BRlist(req);
        if (reqBRlist.CheckReqiredFields()){
            SupportGET service = new SupportGET(reqBRlist);

//            service.Run();
            ResponsModel responsModel = null;
            try {
                responsModel = service.Run(popup);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            resp.getWriter().write(responsModel.getResMassage());
            resp.setStatus(responsModel.getResStatus());

        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Role is Required!");
        }
    }
}
