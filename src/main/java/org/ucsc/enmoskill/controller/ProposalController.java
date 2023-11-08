package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import org.ucsc.enmoskill.Services.ProposalDELETESer;
import org.ucsc.enmoskill.Services.ProposalGETSer;
import org.ucsc.enmoskill.Services.ProposalPOSTSer;
import org.ucsc.enmoskill.Services.ProposalUPDATESer;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Pro_CR;
import org.ucsc.enmoskill.model.ProposalModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;

import static java.lang.System.out;

public class ProposalController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        Connection connection = DatabaseConnection.initializeDatabase();

        Pro_CR proBRlist = new Pro_CR(req);

        out.println("ProposalID: " + proBRlist.getProposalid());
        out.println("UserID: " + proBRlist.getUserid());
        out.println("Role: " + proBRlist.getRole());

        if(proBRlist.CheckReqiredFilds()) {
            if (proBRlist.isClient()){
                if (proBRlist.getUserid() == null){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("User ID is Required!");
                }else{
                    if(proBRlist.getProposalid() == null) {
                        ProposalGETSer service = new ProposalGETSer(resp);
                        service.GetAllProposals(connection ,proBRlist.getUserid());
                    }else{
                        ProposalGETSer service = new ProposalGETSer(resp);
                        service.GetProposal(connection ,proBRlist.getProposalid(),proBRlist.getUserid(),resp );
                    }

                }
            }
        }else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Role is Required!");
        }
   }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try{
            // Create a Gson instance
            Gson gson = new Gson();

            // Read JSON data from the request body
            BufferedReader reader = req.getReader();
            ProposalModel proposal = gson.fromJson(reader, ProposalModel.class);

            Pro_CR proBRlist = new Pro_CR(req);

            out.println("getDuration: " + proposal.getDuration());
            out.println("getDescription: " + proposal.getDescription());
            out.println("getBudget: " + proposal.getBudget());
            out.println("getDate: " + proposal.getDate());
            out.println("getUserID: " + proposal.getUserID());

            if (proposal.getUserID()!=null  && proposal.getBudget()!=null && proposal.getDescription()!=null && proposal.getDuration()!=null){

                ProposalPOSTSer proposalPOSTSer = new ProposalPOSTSer();
                boolean isSuccess = proposalPOSTSer.isInsertionSuccessful(proposal , proBRlist);

                if (isSuccess) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write("Proposal submitted successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("Proposal submitted unsuccessfully");
                }
            }else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Required Field Missing");
            }
        }catch (Exception e) {
            e.printStackTrace(); // Print the exception details for debugging
            throw new RuntimeException(e);
        } finally {
            out.close();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Connection connection = DatabaseConnection.initializeDatabase();

        Pro_CR proBRlist = new Pro_CR(req);

        out.println("ProposalID: " + proBRlist.getProposalid());
        out.println("UserID: " + proBRlist.getUserid());
        out.println("Role: " + proBRlist.getRole());

//        if(proBRlist.CheckReqiredFilds()) {
//            if (proBRlist.isDesigner()){
//                if (proBRlist.getUserid() == null){
//                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    resp.getWriter().write("User ID is Required!");
//                }else{
                    if(proBRlist.getProposalid() == null) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("Proposal ID is Required!");
                    }else{
                        ProposalDELETESer service = new ProposalDELETESer(resp);
                        service.DeleteProposal(connection ,proBRlist.getProposalid(),resp );
                    }
//
//                }
//            }
//        }else {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("Role is Required!");
//        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Connection connection = DatabaseConnection.initializeDatabase();

        Pro_CR proBRlist = new Pro_CR(req);

        // Create a Gson instance
        Gson gson = new Gson();

        // Read JSON data from the request body
        BufferedReader reader = req.getReader();
        ProposalModel proposal = gson.fromJson(reader, ProposalModel.class);

        out.println("ProposalID: " + proBRlist.getProposalid());
        out.println("UserID: " + proBRlist.getUserid());
        out.println("Role: " + proBRlist.getRole());

//        if(proBRlist.CheckReqiredFilds()) {
//            if (proBRlist.isDesigner()){
//                if (proBRlist.getUserid() == null){
//                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    resp.getWriter().write("User ID is Required!");
//                }else{
                    if(proBRlist.getProposalid() == null) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getWriter().write("Proposal ID is Required!");
                    }else{
                        ProposalUPDATESer service = new ProposalUPDATESer(resp);
                        service.UpdateProposal(connection ,proBRlist.getProposalid(),proposal ,resp );
                    }

//                }
//            }
//        }else {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("Role is Required!");
//        }


    }
}
