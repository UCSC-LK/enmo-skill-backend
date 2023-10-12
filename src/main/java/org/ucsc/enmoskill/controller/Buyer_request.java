package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.BuyerRequestGET;
import org.ucsc.enmoskill.model.Req_BRlist;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class Buyer_request extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        BufferedReader reader = req.getReader();
        Gson gson = new Gson();

        try (BufferedReader reader = req.getReader()){
            Req_BRlist reqBRlist = gson.fromJson(reader, Req_BRlist.class);
            if (reqBRlist.CheckReqiredFields()){
                BuyerRequestGET service = new BuyerRequestGET(resp,reqBRlist);
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
}
