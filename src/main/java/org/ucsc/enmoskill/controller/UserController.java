package org.ucsc.enmoskill.controller;

import com.google.gson.JsonObject;
import org.ucsc.enmoskill.Services.BuyerRequestPUT;
import org.ucsc.enmoskill.Services.ClientDetailsPUT;
import org.ucsc.enmoskill.Services.UserSer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.User;

import com.google.gson.Gson;
import org.ucsc.enmoskill.utils.Hash;
import org.ucsc.enmoskill.utils.TokenService;

public class UserController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
            if (usermodel.checkRequired()){
                ClientDetailsPUT service = new ClientDetailsPUT(resp,usermodel);
                service.Run();
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (BufferedReader reader = req.getReader()){
            User usermodel = new Gson().fromJson(reader, User.class);
            if (usermodel.getId()!=0){
                ClientDetailsPUT service = new ClientDetailsPUT(resp,usermodel);
                service.Validate();
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


