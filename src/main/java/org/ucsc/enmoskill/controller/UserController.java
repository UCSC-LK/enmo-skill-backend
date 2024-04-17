package org.ucsc.enmoskill.controller;

import com.google.gson.JsonObject;
import org.ucsc.enmoskill.Services.BuyerRequestPUT;
import org.ucsc.enmoskill.Services.ClientDetailsPUT;
import org.ucsc.enmoskill.Services.UserGet;
import org.ucsc.enmoskill.Services.UserSer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.User;

import com.google.gson.Gson;
import org.ucsc.enmoskill.model.UserFullModel;
import org.ucsc.enmoskill.utils.Hash;
import org.ucsc.enmoskill.utils.TokenService;

public class UserController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TokenService.TokenInfo tokenInfo;

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);//default req is a request of controller

        tokenInfo = tokenService.getTokenInfo(token);

        if (tokenService.isTokenValid(token)){
            if (tokenInfo.isAdmin()){
                // extract query params
                String roleNoParam = req.getParameter("role");
                String statusParam = req.getParameter("status");
                String userIdParam = req.getParameter("userId");

                int roleNo = Integer.parseInt(roleNoParam);


                // CHECK WHETHER THIS PARM EXISTS
                if (userIdParam == null){

                    // convert into integer
                    int status = Integer.parseInt(statusParam);

                    UserSer service = new UserSer();
                    List<UserFullModel> userList1 = new ArrayList<>();
                    List<UserFullModel> userList2 = new ArrayList<>();

                    int recordCount;

                    do{
                        if (roleNo == 2) {
                            // get user data
                            userList1 = service.getAllDesigners();

        resp.setContentType("application/json");
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);
        if (tokenService.isTokenValidState(token) == 1) {
            TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
            if(tokenInfo.getRole().equals("1")||tokenInfo.getRole().equals("3")){
                UserGet userGet = new UserGet(tokenInfo);
                ResponsModel res = userGet.Run();
                resp.setStatus(res.getResStatus());
                resp.getWriter().write(res.getResMassage());
            }else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } else if (tokenService.isTokenValidState(token) == 2) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        int userID;
        try {
            // Create a Gson instance
            Gson gson = new Gson();

            // Read JSON data from the request body
            BufferedReader reader = req.getReader();
            User user = gson.fromJson(reader, User.class);

            // Debugging statements
            System.out.println("Email: " + user.getEmail());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Password: " + user.getPassword());
            System.out.println("user_role: " + user.getUser_role());
            if (user.getPassword().length() < 8) {
                System.out.println("Password length should be at least 8 characters");
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                out.write("Password should be at least 8 characters");
                return;

            }
            // Hash the password
            String hashedPassword = Hash.hashPassword(user.getPassword());
//            userID = (int)(Math.random()*(100000-10+1)+10);
            // Create a new User object with the username and hashed password
            User newUser = new User(user.getEmail(),user.getUsername(), hashedPassword);

            UserSer newuser = new UserSer();
            // Insert data into the database
            int isSuccess = newuser.isInsertionSuccessful(newUser);
            if (isSuccess==1) {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write("Registration successfully");
//                System.out.println("Registration successful");
            } else if(isSuccess==2){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Email or Username already exists");
            }  else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Registration failed");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception details for debugging
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (BufferedReader reader = req.getReader()){
            User usermodel = new Gson().fromJson(reader, User.class);
            TokenService tokenService = new TokenService();
            String token = tokenService.getTokenFromHeader(req);
            if (tokenService.isTokenValidState(token) == 1) {
                TokenService.TokenInfo tokenInfo = tokenService.getTokenInfo(token);
                if(tokenInfo.getRole().equals("1")){
                    usermodel.setId(Integer.parseInt(tokenInfo.getUserId()));
                    ClientDetailsPUT service = new ClientDetailsPUT(resp,usermodel);
                    service.Run();
                }else {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }

            } else if (tokenService.isTokenValidState(token) == 2) {
                resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }


        } catch (Exception e) {
            resp.getWriter().write(e.toString());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }




}


