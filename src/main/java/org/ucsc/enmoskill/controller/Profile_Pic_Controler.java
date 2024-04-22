package org.ucsc.enmoskill.controller;

import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Profile_Pic_Controler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);
        if (tokenService.isTokenValidState(token) == 1) {
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
            Connection connection = DatabaseConnection.initializeDatabase();
            try {
                PreparedStatement preparedStatement =connection.prepareStatement("SELECT url FROM users WHERE userID ="+tokenInfo.getUserId());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    resp.setStatus(HttpServletResponse.SC_OK);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("url",resultSet.getString("url"));
                    resp.getWriter().write(jsonObject.toString());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if (tokenService.isTokenValidState(token) == 2) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }
    }
}
