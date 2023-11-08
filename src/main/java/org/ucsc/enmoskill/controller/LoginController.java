package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.Services.LoginSer;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Login;
import org.ucsc.enmoskill.utils.Hash;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

public class LoginController extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();



        try {
            Gson gson = new Gson();

            // Read JSON data from the request body
            BufferedReader reader = req.getReader();
            Login login = gson.fromJson(reader, Login.class);

            // Debugging statements
            System.out.println("Username: " + login.getEmail());
            System.out.println("Password: " + login.getPassword());


            // Insert data into the database
            LoginSer loginSer = new LoginSer();
            Login loginDB = loginSer.getLoginData(login);
//            out.write("Login successfully");

            System.out.println( "loginDB2 " + loginDB.getPassword());
            System.out.println( "loginDB3 " + loginDB.getEmail());
            System.out.println( "loginDB5 " + loginDB.getId());
            System.out.println( "loginDB6 " + loginDB.getUserLevelID());
            // Check if the provided password matches the stored hashed password
            boolean passwordMatch = Hash.checkPassword(login, loginDB);

            if (passwordMatch) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                String successMessage = "{\"message\": \"Login successfully\", \"userLevelID\": " + loginDB.getUserLevelID() + " , \"userID\": " + loginDB.getId() + "}";
                resp.getWriter().write(successMessage);
                System.out.println("Login successful");
            } else {
                // Set the status code to 401 (Unauthorized) for an unsuccessful login
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json");
                String errorMessage = "{\"message\": \"Login unsuccessfully\"}";
                resp.getWriter().write(errorMessage);
                System.out.println("Login incorrect");
            }


        } catch (Exception e) {
            System.out.println("Hiiii");
            e.printStackTrace(); // Print the exception details for debugging
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }
}
