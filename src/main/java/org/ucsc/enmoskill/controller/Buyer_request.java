package org.ucsc.enmoskill.controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.Services.BuyerRequestGET;
import org.ucsc.enmoskill.model.Req_BRlist;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


public class Buyer_request extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String origin = Dotenv.load().get("ORIGIN");
        resp.setHeader("Access-Control-Allow-Origin", origin);
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
}
