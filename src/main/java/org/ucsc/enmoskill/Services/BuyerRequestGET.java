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

    public void Run(){
        if(request.getRole().equals("Client")){
            GetRequestClient();
        } else if (request.getRole().equals("Designer")) {
            GetRequestDesigner();
        }

    }
    private void GetRequestClient(){

    }
    private void GetRequestDesigner(){
        Connection connection = DatabaseConnection.initializeDatabase();
        try {
            ResultSet result = connection.prepareStatement("SELECT br.requestID, br.userID, br.date, br.discription, br.duration, br.budget, br.status, u.username FROM  `enmo_database`.`buyer_request` AS br JOIN `enmo_database`.`users` AS u ON br.userID = u.userid where br.status =1;").executeQuery();
            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()) {
                BuyerRequestModel buyerRequest = new BuyerRequestModel(result);
                JsonObject jsonObject = gson.toJsonTree(buyerRequest).getAsJsonObject();
                jsonArray.add(jsonObject);
            }
            response.getWriter().write(jsonArray.toString());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
