package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.Req_BRlist;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuyerRequestGET {
    private Req_BRlist request;
    private HttpServletResponse response;
    public BuyerRequestGET(HttpServletResponse response, Req_BRlist reqBRlist) {
        this.response=response;
        request=reqBRlist;
    }

    public void Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            response.getWriter().write("SQL Connection Error");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        if(request.isClient()){
            if (request.getUserid()==null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("User ID is Required!");
                return;
            }
            GetRequestClient(connection ,request.getUserid());
        } else if (request.isDesigner()) {
            GetRequestDesigner(connection );
        }

    }
    private void GetRequestClient( Connection connection , String userid){


        try {
            String query = "SELECT br.requestID, br.userID,br.title, br.date, br.discription, br.duration, br.budget, br.status,br.sample_work_url,  u.username FROM  `enmo_database`.`jobs` AS br JOIN `enmo_database`.`users` AS u ON br.userID = u.userid where br.userID = ? ORDER BY status DESC,date DESC,requestID DESC;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userid);

            ResultSet result = preparedStatement.executeQuery();


            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();
            int Count=0;
            while (result.next()) {
                BuyerRequestModel buyerRequest = new BuyerRequestModel(result);
                if (buyerRequest.getStatus()==1)Count++;
                JsonObject jsonObject = gson.toJsonTree(buyerRequest).getAsJsonObject();
                jsonArray.add(jsonObject);
            }


            JsonObject Main = new JsonObject();
            Main.addProperty("count", Count);
            Main.add("data",jsonArray);

            response.getWriter().write(Main.toString());
            response.setStatus(HttpServletResponse.SC_OK);


        } catch (SQLException | IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);

        }
    }
    private void GetRequestDesigner( Connection connection){

        try {
            ResultSet result = connection.prepareStatement("SELECT br.requestID, br.userID, br.title, br.date, br.discription, br.duration, br.budget, br.status,br.sample_work_url, u.username FROM  `enmo_database`.`jobs` AS br JOIN `enmo_database`.`users` AS u ON br.userID = u.userid where br.status =1 ORDER BY date DESC;").executeQuery();
            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()) {
                BuyerRequestModel buyerRequest = new BuyerRequestModel(result);
                JsonObject jsonObject = gson.toJsonTree(buyerRequest).getAsJsonObject();
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
