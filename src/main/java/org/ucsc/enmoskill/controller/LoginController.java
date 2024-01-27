package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.nimbusds.jwt.JWTClaimsSet;
import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.Services.LoginSer;
import org.ucsc.enmoskill.model.Login;
import org.ucsc.enmoskill.utils.Hash;
import org.ucsc.enmoskill.utils.TokenService;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class LoginController extends HttpServlet {
    static Dotenv dotenv = Dotenv.load();

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

            System.out.println( "loginDB2 " + loginDB.getPassword());
            System.out.println( "loginDB3 " + loginDB.getEmail());
            System.out.println( "loginDB5 " + loginDB.getId());
            System.out.println( "loginDB6 " + loginDB.getUserLevelID());

            // Check if the provided password matches the stored hashed password
            boolean passwordMatch = Hash.checkPassword(login, loginDB);

            if (passwordMatch) {
                try {
                    TokenService tokenService = new TokenService();
                    String token = tokenService.generateToken(loginDB.getId(), loginDB.getUserLevelID());
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.setContentType("application/json");
                    String successMessage = "{\"message\": \"Login successfully\",\"userLevelID\":"+loginDB.getUserLevelID()+" ,\"JWT\": \"" + token+ "\"}";
                    resp.getWriter().write(successMessage);
                    System.out.println("Login successful");
                    System.out.println(token);
               } catch (Exception e) {
                    e.printStackTrace();
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().write("Internal Server Error");
                }




            } else {
                // Set the status code to 401 (Unauthorized) for an unsuccessful login
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json");
                String errorMessage = "{\"message\": \"Login unsuccessfully\"}";
                resp.getWriter().write(errorMessage);
                System.out.println("Login incorrect");
            }
        } catch (Exception e) {

            e.printStackTrace(); // Print the exception details for debugging
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }

}
