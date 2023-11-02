package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.Req_BRlist;
import org.ucsc.enmoskill.model.SupprtModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupportGET {
    private Req_BRlist request;
    private HttpServletResponse response;

    public SupportGET(Req_BRlist request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            response.getWriter().write("SQL Connection Error");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        if(request.isClient()||request.isDesigner()){
//            System.out.println(request.getUserid()+request.getRole());
            if (request.getUserid()==null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("User ID is Required!");
                return;
            }
            GetRequestClient(connection ,request.getUserid());
        }  else if (request.isAgent()) {
            ////////////////////////////////////////////////////////////////
        }
    }

    private void GetRequestClient( Connection connection , String userid){


        try {
            String query = "SELECT t.* FROM enmo_database.ticket t WHERE requesterID = ? ORDER BY status DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userid);

            ResultSet result = preparedStatement.executeQuery();


            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()) {
                SupprtModel supprtModel = new SupprtModel(result);
                JsonObject jsonObject = gson.toJsonTree(supprtModel).getAsJsonObject();
                jsonArray.add(jsonObject);
            }


            response.getWriter().write(jsonArray.toString());
            response.setStatus(HttpServletResponse.SC_OK);


        } catch (SQLException | IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);

        }
    }
}
