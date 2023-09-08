package org.ucsc.enmo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class User_Controler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OutputStream outputStream = resp.getOutputStream();
        BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write("hello  enmoskill");
        bufferedWriter.flush();
        bufferedWriter.close();

    }
}