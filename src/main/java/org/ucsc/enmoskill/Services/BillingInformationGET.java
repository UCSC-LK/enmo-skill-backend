package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import netscape.javascript.JSObject;
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
            JsonObject jsonObject = new JsonObject();
            Gson gson = new Gson();

            if (result.next()){
                BillingInformationModel billingInformationModel = new BillingInformationModel(result);
                jsonObject = gson.toJsonTree(billingInformationModel).getAsJsonObject();
                return new ResponsModel(jsonObject.toString(),HttpServletResponse.SC_OK);
            }else{
                return new ResponsModel("No data available",429);
            }


        }

    }
}
