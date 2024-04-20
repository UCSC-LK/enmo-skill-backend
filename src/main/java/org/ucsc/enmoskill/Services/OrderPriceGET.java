package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.OrderPriceModel;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.SupprtModel;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderPriceGET {
    private TokenService.TokenInfo tokenInfo;

    public OrderPriceGET(TokenService.TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }
    public ResponsModel Run(String packegeID) throws SQLException {
        Connection connection = DatabaseConnection.initializeDatabase();

        if(connection==null){
            return new ResponsModel("SQL Connection Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }else {

            String query = "SELECT t.price FROM enmo_database.package_pricing t WHERE package_id = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, packegeID);

            ResultSet result = preparedStatement.executeQuery();
            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            while (result.next()) {
                OrderPriceModel orderPriceModel = new OrderPriceModel(result);
                JsonObject jsonObject = gson.toJsonTree(orderPriceModel).getAsJsonObject();
                jsonArray.add(jsonObject);
            }
            return new ResponsModel(jsonArray.toString(), HttpServletResponse.SC_OK);
        }
    }

}
