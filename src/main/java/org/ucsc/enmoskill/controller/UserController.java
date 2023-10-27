package org.ucsc.enmoskill.controller;

import org.ucsc.enmoskill.Services.UserTable;
import org.ucsc.enmoskill.database.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ucsc.enmoskill.model.User;

import com.google.gson.Gson;
import org.ucsc.enmoskill.utils.Hash;

public class UserController extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

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

            // Hash the password
            String hashedPassword = Hash.hashPassword(user.getPassword());

            // Create a new User object with the username and hashed password
            User newUser = new User(user.getEmail(),user.getUsername(), hashedPassword);

            // Insert data into the database
            boolean isSuccess = UserTable.isInsertionSuccessful(newUser);
            if (isSuccess) {
                resp.setStatus(HttpServletResponse.SC_OK);
//                out.write("Registration successfully");
//                System.out.println("Registration successful");
            } else {
                // Set the status code to 401 (Unauthorized) for an unsuccessful login
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                out.write("Registration unsuccessfully");
//                System.out.println("Registration incorrect");
            }
            out.write("Data inserted successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception details for debugging
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

}
