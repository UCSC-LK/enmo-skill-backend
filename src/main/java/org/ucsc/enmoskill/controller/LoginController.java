package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.LoginSer;
import org.ucsc.enmoskill.model.Login;
import org.ucsc.enmoskill.model.User;
import org.ucsc.enmoskill.controller.Hash;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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
            System.out.println("Username: " + login.getUsername());
            System.out.println("Password: " + login.getPassword());


            // Insert data into the database
            Login loginDB = LoginSer.getLoginData(login);
//            out.write("Login successfully");

            // Check if the provided password matches the stored hashed password
            boolean passwordMatch = Hash.checkPassword(login, loginDB);

            if (passwordMatch) {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write("Login successfully");
                System.out.println("Login successful");
            } else {
                // Set the status code to 401 (Unauthorized) for an unsuccessful login
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.write("Login unsuccessfully");
                System.out.println("Login incorrect");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Print the exception details for debugging
            throw new RuntimeException(e);
        } finally {
            out.close();
        }
    }
}
