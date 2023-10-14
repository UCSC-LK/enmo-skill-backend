package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.Services.BuyerRequestGET;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.Req_BRlist;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;


public class Buyer_request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setHeader("Access-Control-Allow-Origin", Dotenv.load().get("ORIGIN"));
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
        resp.setContentType("application/json");

        Req_BRlist reqBRlist =new Req_BRlist(req);
        if (reqBRlist.CheckReqiredFields()){
            BuyerRequestGET service = new BuyerRequestGET(resp,reqBRlist);
            service.Run();
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Required Field Missing");
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        try (BufferedReader reader = req.getReader()){
            BuyerRequestModel buyerRequestModel = new Gson().fromJson(reader, BuyerRequestModel.class);
            if (true){

//                BuyerRequestGET service = new BuyerRequestGET(resp,reqBRlist);
//                service.Run();
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
