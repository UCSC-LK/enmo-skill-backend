package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.ChatPOST;
import org.ucsc.enmoskill.Services.ChatsGET;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ChatsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        TokenService tokenService = new TokenService();
        String token= tokenService.getTokenFromHeader(req);
        if (tokenService.isTokenValidState(token)==1){
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
            ChatsGET chatsGET = new ChatsGET(resp,tokenInfo.getUserId());
            try {
                chatsGET.Run();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

        }else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TokenService tokenService = new TokenService();
        String token= tokenService.getTokenFromHeader(req);



        if (tokenService.isTokenValidState(token)==1){
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);

            if(tokenInfo.isDesigner()){
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Designers are not allowed to create chats");
                return;
            }

            if(req.getParameter("receiverID")!=null){
                ChatPOST chatPOST = new ChatPOST(req.getParameter("receiverID"),tokenInfo.getUserId());
                ResponsModel responsModel =chatPOST.Run();
                if(responsModel!=null) {
                        resp.setStatus(responsModel.getResStatus());
                        resp.getWriter().write(responsModel.getResMassage());


                }else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("Critical Error Happened");
                }

            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Required Fields Missing");
            }
        } else if (tokenService.isTokenValidState(token)==2) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}