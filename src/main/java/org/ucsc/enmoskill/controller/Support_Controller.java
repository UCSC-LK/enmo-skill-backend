package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.*;
import org.ucsc.enmoskill.model.Req_BRlist;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

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

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValid(token)){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            try (BufferedReader reader = req.getReader()){

                SupprtModel supportmodel = new Gson().fromJson(reader, SupprtModel.class);

                if (supportmodel.getDescription()!=null&&supportmodel.getSubject()!=null){

                    SupportPOST service = new SupportPOST(supportmodel,tokenInfo);

//                service.Run();
                    ResponsModel responsModel = service.Run();
                    resp.getWriter().write(responsModel.getResMassage());
                    resp.setStatus(responsModel.getResStatus());

                }else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Missing subject or description");
                }
            } catch (Exception e) {
                resp.getWriter().write(e.toString());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValid(token)){

            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            try (BufferedReader reader = req.getReader()){

                SupprtModel supportmodel = new Gson().fromJson(reader, SupprtModel.class);

                if (supportmodel.getDescription()!=null&&supportmodel.getSubject()!=null){

                    SupportPUT service = new SupportPUT(supportmodel,tokenInfo);

//                service.Run();
                    ResponsModel responsModel = service.Run();
                    resp.getWriter().write(responsModel.getResMassage());
                    resp.setStatus(responsModel.getResStatus());
                }else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Missing subject or description");
                }
            } catch (Exception e) {
                resp.getWriter().write(e.toString());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValid(token)){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            String TicketID= req.getParameter("TicketID");

            if(TicketID!=null){
                try {
                    new SupportDELETE(TicketID,tokenInfo,resp);
                } catch (SQLException e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    throw new RuntimeException(e);
                }
            }
            else {
                resp.getWriter().write("Ticket ID required");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValid(token)){

            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            String popup=null;
            String TicketId=null;

            if(req.getParameter("popup")!=null){
                popup= req.getParameter("popup");
            }
            if(req.getParameter("TicketId")!=null){
                TicketId= req.getParameter("TicketId");
            }

            if (tokenInfo.getUserId() != null && tokenInfo.getRole() != null ){
                SupportGET service = new SupportGET(tokenInfo);

//            service.Run();
                ResponsModel responsModel = null;
                try {
                    responsModel = service.Run(popup,TicketId);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                resp.getWriter().write(responsModel.getResMassage());
                resp.setStatus(responsModel.getResStatus());

            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Role is Required!");
            }
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValid(token)){
            TokenService.TokenInfo tokenInfo =tokenService.getTokenInfo(token);
            String agentID = null;
            String decision=null;
            String ticketId=null;

//            Req_BRlist request =new Req_BRlist(req);

            if(tokenInfo.isAgent()){
                SupportOptions service = new SupportOptions(tokenInfo);

                ResponsModel responsModel = null;
                if(req.getParameter("AgentID")!=null && req.getParameter("TicketId")!=null){
                    agentID=req.getParameter("AgentID");
                    ticketId= req.getParameter("TicketId");
                    try {
                        responsModel = service.Run(agentID,ticketId,decision);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    resp.getWriter().write(responsModel.getResMassage());
                    resp.setStatus(responsModel.getResStatus());

                }else if(req.getParameter("Decision")!=null && req.getParameter("TicketId")!=null){
                    decision= req.getParameter("Decision");
                    ticketId= req.getParameter("TicketId");

                    try {
                        responsModel = service.Run(agentID,ticketId,decision);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    resp.getWriter().write(responsModel.getResMassage());
                    resp.setStatus(responsModel.getResStatus());
                }


            }else{
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("You can't access");
            }
        }

    }
}
