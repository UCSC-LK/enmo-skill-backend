package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupportOptions {

    private TokenService.TokenInfo tokenInfo;
    public SupportOptions(TokenService.TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public ResponsModel Run(String agentID, String ticketId, String decision, String toAdmin,String ugent,int userId ,String comment) throws SQLException, IOException {
        Connection connection = DatabaseConnection.initializeDatabase();


        if(connection==null){

//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }else{
            //assign a agent ----------------------------------------------------------------
            if(agentID!= null){
                String query ="UPDATE enmo_database.ticket t SET t.agentID="+agentID+",t.status=2 " +
                        "WHERE t.agentID=0 AND t.assign_ad=0 AND t.ref_no="+ticketId;
                System.out.println(query);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                int row = preparedStatement.executeUpdate(query);

                if(row>0){
                    SendEmail sendEmail = new SendEmail(ticketId,agentID);
                    sendEmail.setdata("agent");
                    return new ResponsModel("Ticket assign successful!", HttpServletResponse.SC_OK);
                }else{
                    return new ResponsModel("Ticket assign failed!", HttpServletResponse.SC_NOT_IMPLEMENTED);
                }

            } else if(decision != null){


                if(tokenInfo.isAdmin()){
                    ResponsModel responsModel = setStatusAdmin(connection,decision,ticketId);
                    return responsModel;
                }else if(tokenInfo.isAgent()){
                    ResponsModel responsModel = setStatusAgent(connection,decision,ticketId);
                    return responsModel;
                }
//        }else if(ticketId != null){
//            Date Today= new Date();
//            String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);
//
//            String query2 ="INSERT INTO enmo_database.ticket_comment (ticket_id, agent_id, date, comment) VALUES (?, ?,?, ?)";
//                PreparedStatement preparedStatement1 = connection.prepareStatement(query2);
//                preparedStatement1.setString(1, ticketId);
//                preparedStatement1.setString(2, tokenInfo.getUserId());
//                preparedStatement1.setString(3, Date);
//                preparedStatement1.setString(4, comment);
//                int row= preparedStatement1.executeUpdate();
//
//            if(row>0){
//                return new ResponsModel("Comment was added", HttpServletResponse.SC_OK);
//            }else{
//                return new ResponsModel("Comment was not added", HttpServletResponse.SC_NOT_IMPLEMENTED);
//            }
            }else if(toAdmin != null){
                ResponsModel responsModel = assignToAgrnt(connection,toAdmin,ticketId,userId ,comment);
                return responsModel;
            }else if (ugent !=null){
                ResponsModel responsModel = markUgent(connection,ugent,ticketId);
                return responsModel;
            }
        }
        return null;
    }
    
    private ResponsModel setStatusAgent(Connection connection , String decision, String ticketId) throws SQLException, IOException {

        String query=null;
        String status=null;

        if(decision.equals("Reject")) {
            query = "UPDATE enmo_database.ticket SET status = 4 " +
                    "WHERE" +
                    " (status = 1 OR (agentID=" + tokenInfo.getUserId() + " AND status = 2 AND assign_ad = 0))" +
                    " AND ref_no=" + ticketId;

            status="reject";
        } else if (decision.equals("Clos")) {

            query = "UPDATE enmo_database.ticket t SET t.status = 3 " +
                    "WHERE" +
                    " (status=1 OR (agentID=" + tokenInfo.getUserId() + " AND status=2 AND assign_ad = 0))" +
                    " AND ref_no=" + ticketId;

            status="close";

        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        int row = preparedStatement.executeUpdate(query);

        if(row>0){

                String userId=null;

                String query1 = "SELECT t.requesterID FROM ticket t WHERE t.ref_no="+ticketId;
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

                ResultSet resultSet = preparedStatement1.executeQuery();
                while (resultSet.next()){
                    userId = resultSet.getString("requesterID");
                }

                SendEmail sendEmail = new SendEmail(ticketId,userId);
                sendEmail.setdata(status);
            return new ResponsModel("The ticket was " + decision+"ed", HttpServletResponse.SC_OK);
        }else{
            connection.rollback(); // Rollback if the second query fails---
            return new ResponsModel("The ticket cannot be "+decision+"ed", HttpServletResponse.SC_NOT_IMPLEMENTED);
        }

//        try {
//            connection.setAutoCommit(false); // Start transaction-----------
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            int row = preparedStatement.executeUpdate(query);
//
//            if(row>0){
//                if(comment != null){
//                    String query2 ="INSERT INTO enmo_database.ticket_comment (ticket_id, agent_id, date, comment) VALUES (?, ?, NOW(), ?)";
//                    PreparedStatement preparedStatement1 = connection.prepareStatement(query2);
//                    preparedStatement1.setString(1, ticketId);
//                    preparedStatement1.setString(2, tokenInfo.getUserId());
//                    preparedStatement1.setString(3, comment);
//                    int row1= preparedStatement1.executeUpdate();
//
//                    if(row1>0){
//                        connection.commit(); // Commit transaction--------------
//                        return new ResponsModel("The ticket was " + decision+"ed", HttpServletResponse.SC_OK);
//                    }else{
//                        connection.rollback(); // Rollback if the second query fails---
//                        return new ResponsModel("The ticket cannot be "+decision+"ed", HttpServletResponse.SC_NOT_IMPLEMENTED);
//                    }
//                }else {
//                    connection.commit();
//                    return new ResponsModel("The ticket was " + decision + "ed", HttpServletResponse.SC_OK);
//                }
//            }else{
//                connection.rollback();
//                return new ResponsModel("The ticket cannot be "+decision+"ed", HttpServletResponse.SC_NOT_IMPLEMENTED);
//            }
//        } catch (SQLException e) {
//            connection.rollback(); // Rollback if there's an exception
//            throw e; // Re-throw the exception after rollback
//        } finally {
//            connection.setAutoCommit(true); // Reset auto-commit mode
//        }

    }

    private ResponsModel setStatusAdmin(Connection connection , String decision, String ticketId) throws SQLException, IOException {

        String query=null;
        String status=null;

        if(decision.equals("Reject")) {
            query =  "UPDATE enmo_database.ticket t " +
                    "SET t.status = 4 " +
                    "WHERE (t.status = 2 OR t.status = 1) AND t.assign_ad=1 AND t.ref_no= "+ ticketId;
            status = "reject";

        } else if (decision.equals("Clos")) {
            query = "UPDATE enmo_database.ticket t " +
                    "SET t.status = 3 " +
                    "WHERE (t.status = 2 OR t.status = 1) AND t.assign_ad=1 AND t.ref_no= "+ ticketId;
            status = "close";
        }

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            int row = preparedStatement.executeUpdate(query);

            if (row > 0) {
                String userId=null;

                String query1 = "SELECT t.requesterID FROM ticket t WHERE t.ref_no="+ticketId;
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);

                ResultSet resultSet = preparedStatement1.executeQuery();
                while (resultSet.next()){
                    userId = resultSet.getString("requesterID");
                }

                SendEmail sendEmail = new SendEmail(ticketId,userId);
                sendEmail.setdata(status);

                return new ResponsModel("The ticket was " + decision + "ed", HttpServletResponse.SC_OK);
            } else {
                return new ResponsModel("The ticket cannot be " + decision + "ed", HttpServletResponse.SC_NOT_IMPLEMENTED);
            }
    }

    private ResponsModel assignToAgrnt(Connection connection ,String toadmin, String ticketId,int userId ,String comment) throws SQLException {
//        Statement statement = null;
//        try {
//            connection.setAutoCommit(false);
//            statement = connection.createStatement();
//
//            String query = "UPDATE enmo_database.ticket t " +
//                    "SET t.assign_ad = 1 " +
//                    "WHERE (t.assign_ad = 0 AND !(t.status=3 OR t.status=4) AND t.ref_no ="+ ticketId +")";
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            int row= preparedStatement.executeUpdate();
//
//            String query2 = "INSERT INTO enmo_database.toAdmin (ticketId, userID, agentID, comment) VALUES (?, ?, ?,?)";
//            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
//            preparedStatement2.setString(1, ticketId);
//            preparedStatement2.setString(2, userID);
//            preparedStatement2.setString(3, tokenInfo.getUserId());
//            preparedStatement2.setString(4, comment);
//
//            int row2= preparedStatement.executeUpdate();
//        connection = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        int rowsAffected1=0;
        int rowsAffected2=0;

        try {
            // Set auto-commit to false to start a transaction
            connection.setAutoCommit(false);

            // First query
            String query = "UPDATE enmo_database.ticket t " +
                    "SET t.assign_ad = 1 " +
                    "WHERE (t.assign_ad = 0 AND !(t.status=3 OR t.status=4) AND t.ref_no = ?)";
            stmt1 = connection.prepareStatement(query);
            stmt1.setInt(1, Integer.parseInt(ticketId)); // Assuming ticketId is declared and assigned

            rowsAffected1 = stmt1.executeUpdate();

            // Second query
            String query2 = "INSERT INTO enmo_database.toAdmin (ticketId, userID, agentID, comment) VALUES (?, ?, ?, ?)";
            stmt2 = connection.prepareStatement(query2);
            stmt2.setInt(1, Integer.parseInt(ticketId)); // Assuming ticketId is declared and assigned
            stmt2.setInt(2, userId); // Assuming userId is declared and assigned
            stmt2.setInt(3, Integer.parseInt(tokenInfo.getUserId())); // Assuming agentId is declared and assigned
            stmt2.setString(4, comment); // Assuming comment is declared and assigned

            rowsAffected2 = stmt2.executeUpdate();
            System.out.println(rowsAffected1);
            System.out.println(rowsAffected2);
            // If both queries executed successfully, commit the transaction
            if(rowsAffected1>0 && rowsAffected2>0){
                connection.commit();
                return new ResponsModel("Assign successful!", HttpServletResponse.SC_OK);
            }else{
                connection.rollback();
                return new ResponsModel("Assign unsuccessful!", HttpServletResponse.SC_NOT_IMPLEMENTED);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // If an exception occurred, rollback the transaction
                if (connection != null)
                    connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                // Close prepared statements
                if (stmt1 != null)
                    stmt1.close();
                if (stmt2 != null)
                    stmt2.close();
                // Set auto-commit back to true
                if (connection != null)
                    connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

            

//        }catch (SQLException e) {
//            e.printStackTrace();
//            if (connection != null) {
//                try {
//                    // 5. Rollback transaction if any query fails
//                    System.err.println("Transaction is being rolled back");
//                    connection.rollback();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        } finally {
//            try {
//                if (statement != null) {
//                    statement.close();
//                }
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//



//        if(row>0){
//            return new ResponsModel("Assign successful!", HttpServletResponse.SC_OK);
//        }else{
//            return new ResponsModel("Assign unsuccessful!", HttpServletResponse.SC_NOT_IMPLEMENTED);
//        }
        return new ResponsModel("Assign unsuccessful!", HttpServletResponse.SC_NOT_IMPLEMENTED);

    }

    private ResponsModel markUgent(Connection connection,String urgent,String ticketId) throws SQLException {
        String query = "UPDATE enmo_database.ticket t " +
                        "SET t.urgent = 1 " +
                        "WHERE (t.urgent = 0 AND !(t.status=3 OR t.status=4) AND t.ref_no ="+ ticketId +")";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        int row= preparedStatement.executeUpdate();

        if(row>0){
            return new ResponsModel("Marked", HttpServletResponse.SC_OK);
        }else{
            return new ResponsModel("Cannot mark", HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }
}
