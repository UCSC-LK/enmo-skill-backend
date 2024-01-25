package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.Req_BRlist;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupportGET {
    private Req_BRlist request;


    public SupportGET(Req_BRlist request) {
        this.request = request;
        //this.response = response;
    }

    public ResponsModel Run(String popup) throws IOException, SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();

        if(connection==null){

//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }else {
            if (request.isClient() || request.isDesigner()) {

                if (request.getUserid() == null) {
//
//                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    response.getWriter().write("User ID is Required!");
                    return new ResponsModel("User ID is Required!",HttpServletResponse.SC_BAD_REQUEST);
                }

                ResponsModel responsModel = GetRequestClient(connection, request.getUserid(), popup);
                return responsModel;

            } else if (request.isAgent()) {
                ////////////////////////////////////////////////////////////////
//                return new ResponsModel("Not implemented",HttpServletResponse.SC_NOT_FOUND);
            }
        }

        return new ResponsModel("rong user ID",HttpServletResponse.SC_BAD_REQUEST);
    }

    private ResponsModel GetRequestClient(Connection connection , String userid, String popup) throws SQLException {


            String query;
            PreparedStatement preparedStatement = null;

            if(popup != null){

                query = "SELECT t.* ,t.ticketID FROM enmo_database.ticket_history t WHERE ticketID = ? ORDER BY updateID DESC";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, popup);
                System.out.println(query);


            } else{
                query = "SELECT t.* FROM enmo_database.ticket t WHERE requesterID = ? ORDER BY status DESC";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, userid);
                System.out.println(query);

            }

            ResultSet result = preparedStatement.executeQuery();

            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            if(popup != null){
                while (result.next()) {
                    SupprtModel supprtModel = new SupprtModel(result,popup);
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
}
