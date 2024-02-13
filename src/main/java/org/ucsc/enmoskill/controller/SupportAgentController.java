package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.SupportAgentGET;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupportAgentModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class SupportAgentController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        resp.setContentType("application/json");

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if(tokenService.isTokenValid(token)) {
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);

            if (tokenInfo.getUserId() != null && tokenInfo.getRole() != null) {
                SupportAgentGET supportAgentGET = new SupportAgentGET(tokenInfo);

                try {
                    ResponsModel respons = supportAgentGET.Run();
                    resp.getWriter().write(respons.getResMassage());
                    resp.setStatus(respons.getResStatus());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
