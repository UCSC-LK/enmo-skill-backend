package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BillingInformationModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingInformationGET {
    private TokenService.TokenInfo tokenInfo;

    public BillingInformationGET(TokenService.TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public ResponsModel Run() throws SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();

        if(connection==null){

//            response.getWriter().write("SQL Connection Error");
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }else {
            String query = "SELECT t.* FROM enmo_database.billingInfor t WHERE userID = "+tokenInfo.getUserId();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet result = preparedStatement.executeQuery();
            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()){
                BillingInformationModel billingInformationModel = new BillingInformationModel(result);
                JsonObject jsonObject = gson.toJsonTree(billingInformationModel).getAsJsonObject();
                jsonArray.add(jsonObject);
            }

            return new ResponsModel(jsonArray.toString(),HttpServletResponse.SC_OK);
        }

    }
}
