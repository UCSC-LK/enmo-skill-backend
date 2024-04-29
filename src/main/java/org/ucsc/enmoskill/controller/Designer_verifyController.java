package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.DesignerVerifyModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Designer_verifyController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValidState(token)==1) {
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
            if(tokenInfo.isAdmin()){
                Connection connection = DatabaseConnection.initializeDatabase();
                String query = "select fname,lname,email,phone_no,address,url,NIC,nic_front,nic_back,d.userid as userid from designer_additional_info join designer d on d.userid = designer_additional_info.userid join users u on u.userID = d.userid where designer_additional_info.status =0;";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    JsonArray jsonArray = new JsonArray();
                    while (resultSet.next()) {
                        DesignerVerifyModel designerVerifyModel = new DesignerVerifyModel(resultSet);
                        jsonArray.add(new Gson().toJsonTree(designerVerifyModel));
                    }
                    resp.getWriter().write(jsonArray.toString());
                } catch (SQLException e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValidState(token)==1) {
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
            if(tokenInfo.isClient()){
                try (BufferedReader reader = req.getReader()) {
                    DesignerVerifyModel designerVerifyModel = new Gson().fromJson(reader, DesignerVerifyModel.class);
                    System.out.println(designerVerifyModel.getAddress());
                    System.out.println(designerVerifyModel.getNicfront());
                    System.out.println(designerVerifyModel.getNicback());
                    if (designerVerifyModel.getAddress() != null && designerVerifyModel.getNicfront() != null && designerVerifyModel.getNicback() != null ) {
                        Connection connection = DatabaseConnection.initializeDatabase();
                        String query = "INSERT INTO enmo_database.designer_additional_info (userid, nic_front, nic_back,address) VALUES (?, ?, ?,?)";
                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setInt(1, Integer.parseInt( tokenInfo.getUserId()));
                            preparedStatement.setString(2, designerVerifyModel.getNicfront());
                            preparedStatement.setString(3, designerVerifyModel.getNicback());
                            preparedStatement.setString(4, designerVerifyModel.getAddress());
                            int Rowsaffected = preparedStatement.executeUpdate();
                            if (Rowsaffected > 0) {
                                resp.setStatus(HttpServletResponse.SC_OK);
                            } else {
                                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                            }

                        } catch (SQLException e) {
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } catch (Exception e) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValidState(token)==1) {
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);

            System.out.println(tokenInfo.isAdmin());
            if(tokenInfo.isAdmin()){
                String designerID = req.getParameter("designerID");
                String status = req.getParameter("status");
                if(designerID!=null) {
                    Connection connection = DatabaseConnection.initializeDatabase();
                    String query = "UPDATE enmo_database.designer_additional_info SET status = ? WHERE userid = ?";
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, Integer.parseInt(status));
                        preparedStatement.setInt(2, Integer.parseInt(designerID));
                        int Rowsaffected = preparedStatement.executeUpdate();
                        if (Rowsaffected > 0 && status.equals("1")) {
                            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE user_level_mapping SET userlevelID = 2 WHERE userid = ?");
                            int rows = preparedStatement1.executeUpdate();
                            if(rows>0) {
                                resp.setStatus(HttpServletResponse.SC_OK);
                            }
                        } else if (Rowsaffected > 0 ) {
                            resp.setStatus(HttpServletResponse.SC_OK);
                        }else {
                            resp.setStatus(HttpServletResponse.SC_CONFLICT);
                        }

                    } catch (SQLException e) {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                }else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

            }else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

        }else if(tokenService.isTokenValidState(token)==2){
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String param=null;
        try {
            param = req.getParameter("first_time");
        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        if (tokenService.isTokenValidState(token)==1) {
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
            if (tokenInfo.isClient()) {
                if (param != null) {
                    if (param.equals("true")) {
                        Connection connection = DatabaseConnection.initializeDatabase();
                        String query = "SELECT count(*) as count FROM enmo_database.designer_additional_info WHERE userid = ?";
                        try {
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setInt(1, Integer.parseInt(tokenInfo.getUserId()));
                            ResultSet resultSet = preparedStatement.executeQuery();
                            if (resultSet.next()) {
                                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                            } else {
                                resp.setStatus(HttpServletResponse.SC_OK);
                            }
                        } catch (SQLException e) {
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }else if(tokenService.isTokenValidState(token)==2) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }else{
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
