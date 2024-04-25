package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.OrderPaymentModel;
import org.ucsc.enmoskill.utils.Payment_hashGen;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class PaymentDetailsGET {
    TokenService.TokenInfo tokenInfo;
    HttpServletResponse response;
    String orderId;


    public PaymentDetailsGET(HttpServletResponse response, TokenService.TokenInfo tokenInfo , String orderId) {
        this.response = response;
        this.tokenInfo = tokenInfo;
        this.orderId = orderId;
    }

    public void Run()
    {
        Dotenv dotenv = Dotenv.load();
        if (tokenInfo.isClient()) {
            try {
                String merchantId = dotenv.get("MERCHANT_ID");
                System.out.println(merchantId);
                String currency = "LKR";
                String merchantSecret = dotenv.get("MERCHANT_SECRET");
                System.out.println(merchantSecret);









                String query = "select orders.client_userID,orders.status, orders.order_id,p.title,p.cover_url,pp.type,orders.price as total_price , pp.price as package_price,(select percentage from client_charges where charge_category = 1 ) as usercharge from orders join package p on orders.package_id = p.package_id join package_pricing pp on orders.price_package_id = pp.price_package_id and orders.package_id = pp.package_id where orders.order_id = ?";
                Connection connection = DatabaseConnection.initializeDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, orderId);
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    OrderPaymentModel orderPaymentModel = new OrderPaymentModel(result);
                    if(orderPaymentModel.getClient_userID().equals(tokenInfo.getUserId())&& orderPaymentModel.getStatus()==0){
                        response.setStatus(HttpServletResponse.SC_OK);
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.toJsonTree(orderPaymentModel).getAsJsonObject();
                        System.out.println(orderPaymentModel.getTotal_price());
                        System.out.println(orderPaymentModel.getOrderId());

                        Payment_hashGen payment_hashGen = new Payment_hashGen(merchantId, orderPaymentModel.getOrderId(), orderPaymentModel.getTotal_price(), currency, merchantSecret);
                        String hash = payment_hashGen.createHash();
                        jsonObject.addProperty("hash", hash);

                        response.getWriter().write(jsonObject.toString());
                    }else if(orderPaymentModel.getStatus()!=0) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                    else {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else  {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
