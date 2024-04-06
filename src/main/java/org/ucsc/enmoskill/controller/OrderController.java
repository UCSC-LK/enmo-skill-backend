package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.Services.OrderService;
import org.ucsc.enmoskill.model.Order;
import org.ucsc.enmoskill.utils.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class OrderController extends HttpServlet {

    private TokenService.TokenInfo tokenInfo;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        int clientId = Integer.parseInt(tokenInfo.getUserId());

        if (tokenService.isTokenValid(token)){
            Gson gson = new Gson();

            // creating a Order object using the request body
            BufferedReader reader = req.getReader();
            Order newOrder = gson.fromJson(reader,Order.class);

            newOrder.setClientId(clientId);

            OrderService service = new OrderService();

            newOrder = service.setFee(newOrder);

            int result = service.createOrder(newOrder);

            // Create a JSON object to represent the result
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("orderId", result);

            if (result>0){
                resp.setStatus(HttpServletResponse.SC_OK);
                resultJson.addProperty("message", "Order created successfully");
                System.out.println("Order created successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resultJson.addProperty("message", "Order didn't created");
                System.out.println("Order didn't created");
            }

            // Send the JSON object as the response
            out.write(resultJson.toString());

        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
