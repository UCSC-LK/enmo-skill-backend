package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import com.sun.org.apache.xpath.internal.operations.Or;
import org.ucsc.enmoskill.Services.OrderService;
import org.ucsc.enmoskill.Services.PaymentService;
import org.ucsc.enmoskill.model.Order;
import org.ucsc.enmoskill.model.Payment;
import org.ucsc.enmoskill.utils.TokenService;
import software.amazon.awssdk.awscore.util.SignerOverrideUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.System.out;

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

            OrderService service = new OrderService(resp);

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
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        Gson gson = new Gson();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        int userId = Integer.parseInt(tokenInfo.getUserId());

        Integer orderId = null; // Initialize orderId as null

        String orderIdParam = req.getParameter("orderId");
        if (orderIdParam != null && !orderIdParam.isEmpty()) {
            try {
                orderId = Integer.parseInt(orderIdParam);
            } catch (NumberFormatException e) {
                // Handle the case where orderIdParam cannot be parsed into an integer
                e.printStackTrace(); // Log the error
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Invalid orderId parameter"); // Return an error response
                return; // Exit the method
            }
        }


        if (tokenService.isTokenValid(token)){
            if (tokenInfo.isClient() || tokenInfo.isDesigner()){
                if (orderId == null){
                    System.out.println("Ordersssss details found");
                    OrderService service = new OrderService(resp);
                    service.getAllDesignerOrderDetails(userId);
                } else {
                    OrderService service = new OrderService(resp);
                    Order order = service.getOrderDetails(orderId);

                    if (order != null){
                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.write(gson.toJson(order));
                        out.println("Orders : " + order);
                        System.out.println("Order details found");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("Order details not found");
                        out.println("NoOrders : " );
                        System.out.println("Order details not found");
                    }

                }

            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        int clientId = Integer.parseInt(tokenInfo.getUserId());
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        if (tokenService.isTokenValid(token)){

            if (tokenInfo.isClient()){

                Gson gson = new Gson();

                // creating an Order object using the request body
                BufferedReader reader = req.getReader();
                Order order = gson.fromJson(reader,Order.class);

                OrderService service = new OrderService(resp);

                int result1 = service.updateOrder(order);
//                System.out.println(result1);

                if (result1 > 0){

                    double totalPrice = service.calTotalPayAmount(order);
//                    System.out.println(totalPrice);
                    Payment newPayment = new Payment(totalPrice, orderId);

                    //store the payment
                    PaymentService paymentService = new PaymentService();
                    int result2 = paymentService.savePaymentDetails(newPayment);

                    if (result2 > 0) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.write("Payment successful");
                        System.out.println("Payment successful");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("Payment unsuccessful");
                        System.out.println("Payment unsuccessful");
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Payment unsuccessful");
                    System.out.println("Payment unsuccessful");
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);

        tokenInfo = tokenService.getTokenInfo(token);

        int clientId = Integer.parseInt(tokenInfo.getUserId());
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        if (tokenService.isTokenValid(token)) {

            OrderService service = new OrderService(resp);

            int result = service.deleteOrder(orderId);

            if (result > 0){
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write("Order deleted successfully");
                System.out.println("Order deleted successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Order deletion unsuccessful");
                System.out.println("Order deletion unsuccessful");
            }

        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("Authorization failed");
            System.out.println("Authorization failed");
        }


    }
}
