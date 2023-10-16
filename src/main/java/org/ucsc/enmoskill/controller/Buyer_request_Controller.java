package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.Services.BuyerRequestDELETE;
import org.ucsc.enmoskill.Services.BuyerRequestGET;
import org.ucsc.enmoskill.Services.BuyerRequestPOST;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.Req_BRlist;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;


public class Buyer_request_Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("application/json");

        Req_BRlist reqBRlist =new Req_BRlist(req);
        if (reqBRlist.CheckReqiredFields()){
            BuyerRequestGET service = new BuyerRequestGET(resp,reqBRlist);
            service.Run();
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Role is Required!");
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        try (BufferedReader reader = req.getReader()){
            BuyerRequestModel buyerRequestModel = new Gson().fromJson(reader, BuyerRequestModel.class);
            if (buyerRequestModel.getDiscription()!=null&&buyerRequestModel.getDuration()!=0&&buyerRequestModel.getUserID()!=0&&buyerRequestModel.getBudget()!=0){
                BuyerRequestPOST service = new BuyerRequestPOST(resp,buyerRequestModel);
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
        String reqesttid= req.getParameter("requestID");
        if(reqesttid!=null){
            try {
                new BuyerRequestDELETE(reqesttid,resp);
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new RuntimeException(e);
            }
        }
        else {
            resp.getWriter().write("Invalid Parameter");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
