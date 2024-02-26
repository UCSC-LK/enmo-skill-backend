package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Req_BRlist;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.annotation.processing.SupportedOptions;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupportGET {
    //private Req_BRlist request;
    private TokenService.TokenInfo tokenInfo;

    public SupportGET(TokenService.TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
        //this.response = response;
    }

    public ResponsModel Run(String popup, String TicketId, String comment) throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();


        if(connection==null){

//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }else {
            if (tokenInfo.isClient() || tokenInfo.isDesigner()) {

//                if (request.getUserid() == null) {
////
////                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
////                    response.getWriter().write("User ID is Required!");
//                    return new ResponsModel("Please log in again!",HttpServletResponse.SC_BAD_REQUEST);
//                }

                ResponsModel responsModel = GetRequestClient(connection, tokenInfo.getUserId(), popup,TicketId);
                return responsModel;

            } else if (tokenInfo.isAgent() || tokenInfo.isAdmin()) {
//                if (tokenInfo.getUserId() == null) {
////
////                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
////                    response.getWriter().write("User ID is Required!");
//                    return new ResponsModel("Please log in again!",HttpServletResponse.SC_BAD_REQUEST);
//                }

                ResponsModel responsModel = GetRequestAgent(connection, tokenInfo.getUserId(), popup,TicketId,comment);
                return responsModel;
            }else{
                return new ResponsModel("Invalid user ID",HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private ResponsModel GetRequestClient(Connection connection , String userid, String popup, String TicketId) throws SQLException {

            String query;
            PreparedStatement preparedStatement = null;

            if(popup != null){
                System.out.println(userid);
                query = "SELECT t.* FROM enmo_database.ticket_history t WHERE ticketID = ? AND requesterID = ? ORDER BY updateID";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, popup);
                preparedStatement.setString(2, userid);
            } else if(TicketId != null){

                query = "SELECT t.description,t.subject,t.date,t.status,t.order,t.packages,t.fileURL FROM enmo_database.ticket t WHERE ref_no = ? AND requesterID = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, TicketId);
                preparedStatement.setString(2, userid);

            }else{
                query = "SELECT t.* FROM enmo_database.ticket t WHERE requesterID = ? ORDER BY status";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, userid);
            }

            ResultSet result = preparedStatement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            if(popup != null){
                while (result.next()){
                    SupprtModel supprtModel = new SupprtModel(result,popup);
                    JsonObject jsonObject = gson.toJsonTree(supprtModel).getAsJsonObject();
                    jsonArray.add(jsonObject);
                }

            }else if(TicketId != null){
                while (result.next()) {
                    SupprtModel supprtModel = new SupprtModel(result, TicketId, 1);
                    JsonObject jsonObject = gson.toJsonTree(supprtModel).getAsJsonObject();
                    jsonArray.add(jsonObject);
                }

            }else{
                while (result.next()) {
                    SupprtModel supprtModel = new SupprtModel(result);
                    JsonObject jsonObject = gson.toJsonTree(supprtModel).getAsJsonObject();
                    jsonArray.add(jsonObject);
                }
            }

//            response.getWriter().write(jsonArray.toString());
//            response.setStatus(HttpServletResponse.SC_OK);
            return new ResponsModel(jsonArray.toString(),HttpServletResponse.SC_OK);

    }


    @NotNull

    private ResponsModel GetRequestAgent(Connection connection , String userid, String popup, String TicketId,String comment) throws SQLException {
    //**********user Id for filter specific user's ticket************

        String query;
        PreparedStatement preparedStatement = null;


        if(popup != null){

            query = "SELECT th.* FROM enmo_database.ticket_history th JOIN ticket t ON th.ticketID = t.ref_no WHERE ticketID = ? ORDER BY updateID";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, popup);

        }else if(TicketId != null){
            query = "SELECT t.*, ul.userlevelID, u.username, u.email,u.url FROM enmo_database.ticket t JOIN user_level_mapping ul ON t.requesterID = ul.userID JOIN users u ON t.requesterID = u.userID WHERE t.ref_no = "+TicketId+" ORDER BY status";
            preparedStatement = connection.prepareStatement(query);
        }else if(comment != null) {
            query = "SELECT t.date, t.agent_id, t.comment FROM enmo_database.ticket_comment t where t.ticket_id = ? ORDER BY t.comment_id";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, comment);

        }else{
            query = "SELECT t.*, ul.userlevelID, u.username, u.email,u.url FROM enmo_database.ticket t JOIN user_level_mapping ul ON t.requesterID = ul.userID JOIN users u ON t.requesterID = u.userID ORDER BY status";
            preparedStatement = connection.prepareStatement(query);
        }


        ResultSet result = preparedStatement.executeQuery();

        JsonArray jsonArray = new JsonArray();
        Gson gson = new Gson();

        if(popup != null){
            while (result.next()){
                SupprtModel supprtModel = new SupprtModel(result,popup);
                JsonObject jsonObject = gson.toJsonTree(supprtModel).getAsJsonObject();
                jsonArray.add(jsonObject);
            }

        }else if(comment != null){
            while (result.next()) {
                SupprtModel supprtModel = new SupprtModel(result,comment,true);
                JsonObject jsonObject = gson.toJsonTree(supprtModel).getAsJsonObject();
                jsonArray.add(jsonObject);
            }
        }else{
            while (result.next()) {
                SupprtModel supprtModel = new SupprtModel(result,true);
                JsonObject jsonObject = gson.toJsonTree(supprtModel).getAsJsonObject();
                jsonArray.add(jsonObject);
            }
        }

        return new ResponsModel(jsonArray.toString(),HttpServletResponse.SC_OK);
    }
}
