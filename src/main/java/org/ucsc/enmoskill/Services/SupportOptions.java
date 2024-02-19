package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupportOptions {

    private TokenService.TokenInfo tokenInfo;
    public SupportOptions(TokenService.TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public ResponsModel Run(String agentID, String ticketId, String decision) throws SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();


        if(connection==null){

//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }

        //assign a agent ----------------------------------------------------------------
        if(agentID!= null){
            String query ="UPDATE enmo_database.ticket t SET t.agentID=\'"+agentID+"\',t.status=2 \n" +
                          "WHERE t.agentID=0 and t.ref_no="+ticketId;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int row = preparedStatement.executeUpdate(query);

            if(row>0){
                return new ResponsModel("Ticket assign successful", HttpServletResponse.SC_OK);
            }else{
                return new ResponsModel("Ticket assign failed", HttpServletResponse.SC_NOT_IMPLEMENTED);
            }

        } else if(decision != null){


            if(tokenInfo.isAdmin()){
                ResponsModel responsModel = setStatusAdmin(connection,decision,ticketId);
                return responsModel;
            }else if(tokenInfo.isAgent()){
                ResponsModel responsModel = setStatusAgent(connection,decision,ticketId);
                return responsModel;
            }

        }
        
        return null;
    }
    
    private ResponsModel setStatusAgent(Connection connection , String decision, String ticketId) throws SQLException {

        String query=null;

        if(decision.equals("Reject")) {
            query = "UPDATE enmo_database.ticket SET status = 4 " +
                    "WHERE" +
                    " (status = 0 OR (agentID=" + tokenInfo.getUserId() + " AND status = 2 AND assign_ad = 0))" +
                    " AND ref_no=" + ticketId;

//            query =  "UPDATE enmo_database.ticket t " +
//                    "LEFT JOIN ticket_admin ta ON (t.ref_no = ta.ticket_id AND t.status = 2) " +
//                    "SET t.status = 4 " +
//                    "WHERE ((t.status = 2 AND ta.ticket_id !="+ ticketId +" AND agentID=" + tokenInfo.getUserId() + ") OR (t.status = 1 AND t.ref_no = "+ticketId+"))";

        } else if (decision.equals("Clos")) {

            query = "UPDATE enmo_database.ticket t SET t.status = 3 " +
                    "WHERE" +
                    " (status=0 OR (agentID=" + tokenInfo.getUserId() + " AND status=2))" +
                    " AND ref_no=" + ticketId;
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        int row = preparedStatement.executeUpdate(query);

        if(row>0){
            return new ResponsModel("The ticket was " + decision+"ed", HttpServletResponse.SC_OK);
        }else{
            return new ResponsModel("The ticket cannot be "+decision+"ed", HttpServletResponse.SC_NOT_IMPLEMENTED);
        }

    }

    private ResponsModel setStatusAdmin(Connection connection , String decision, String ticketId) throws SQLException {

        String query=null;

        if(decision.equals("Reject")) {
            query =  "UPDATE enmo_database.ticket t " +
                    "LEFT JOIN ticket_admin ta ON (t.ref_no = ta.ticket_id AND t.status = 2) " +
                    "SET t.status = 4 " +
                    "WHERE ((t.status = 2 AND ta.ticket_id ="+ ticketId +") OR (t.status = 1 AND t.ref_no = "+ticketId+"))";
            

        } else if (decision.equals("Clos")) {

            query = "UPDATE enmo_database.ticket t " +
                    "LEFT JOIN ticket_admin ta ON (t.ref_no = ta.ticket_id AND t.status = 2) " +
                    "SET t.status = 3 " +
                    "WHERE ((t.status = 2 AND ta.ticket_id ="+ ticketId +") OR (t.status = 1 AND t.ref_no = "+ticketId+"))";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        int row = preparedStatement.executeUpdate(query);


        if(row>0){
            return new ResponsModel("The ticket was " + decision+"ed", HttpServletResponse.SC_OK);
        }else{
            return new ResponsModel("The ticket cannot be "+decision+"ed", HttpServletResponse.SC_NOT_IMPLEMENTED);
        }

    }
}
