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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // function [Validuser] parameters => (Username ,password) Return {true false}

        //if(Validuser(usrname,password)){
        //////*   response=> validuser*////
        //OutputStream outputStream = resp.getOutputStream();
        //        BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream));
        //        bufferedWriter.write("Valid user");
        //        bufferedWriter.flush();
        //        bufferedWriter.close();
        //}
        //else{
        // response => invalid user
        // }
        super.doPost(req, resp);
        //1
        //1
        //1
        //1
        //1
        //1v
        //1

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}