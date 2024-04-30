package org.ucsc.enmoskill.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import com.sun.org.apache.xpath.internal.operations.Or;
import com.google.gson.JsonSyntaxException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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

        try {
            TokenService tokenService = new TokenService();
            String token = tokenService.getTokenFromHeader(req);

            System.out.println("token" + token);
            if (token != null) {
            if (tokenService.isTokenValidState(token) == 1){

                tokenInfo = tokenService.getTokenInfo(token);

                int clientId = Integer.parseInt(tokenInfo.getUserId());
                System.out.println("clientId" + clientId);
                if (tokenInfo.isClient() || tokenInfo.isDesigner()){
                    Gson gson = new Gson();
                    System.out.println("test1");
                    // creating a Order object using the request body
                    BufferedReader reader = req.getReader();
                    Order newOrder = gson.fromJson(reader,Order.class);

                    newOrder.setClientId(clientId);
                    System.out.println("newOrder  " + newOrder.getOrderId());
                    System.out.println("newOrder  " + newOrder.getRequirements());
                    System.out.println("newOrder  " + newOrder.getDeliveryDuration());
                    System.out.println("newOrder  " + newOrder.getProposalID());
                    System.out.println("newOrder  " + newOrder.getPrice());
                    System.out.println("newOrder  " + newOrder.getStatus());
                    System.out.println("newOrder  " + newOrder.getDesignerId());
                    System.out.println("newOrder  " + newOrder.getPlatformFeeId());
                    System.out.println("newOrder  " + newOrder.getPackageId());

                    OrderService service = new OrderService(resp);

//                newOrder = service.setFee(newOrder);
                        int result = service.createOrder(newOrder);

                    System.out.println("result" + result);
                        // Create a JSON object to represent the result
                        JsonObject resultJson = new JsonObject();
                        resultJson.addProperty("orderId", result);

                        if (result>0){
                            resp.setStatus(HttpServletResponse.SC_OK);
                            resultJson.addProperty("message", "Order created successfully");
                            System.out.println("Order created successfully");
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resultJson.addProperty("message", "Order didn't created");
                            System.out.println("Order didn't created");
                        }
                        // Send the JSON object as the response
                        out.write(resultJson.toString());

                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }

                } else if (tokenService.isTokenValidState(token) == 2) {
                    resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println("No JWT token found in the request header");
            }
        } catch (ExpiredJwtException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write(createErrorJson("Invalid JSON format", e.getMessage()).toString());
        } catch (SecurityException e) {
            resp.setStatus(e.getMessage().equals("Access denied") ? HttpServletResponse.SC_FORBIDDEN : HttpServletResponse.SC_UNAUTHORIZED);
            out.write(createErrorJson("Security error", e.getMessage()).toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write(createErrorJson("Server error", e.toString()).toString());
        } finally {
            out.close();
        }

    }

    private JsonObject createErrorJson(String errorType, String message) {
        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("errorType", errorType);
        errorJson.addProperty("message", message);
        return errorJson;
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
            if (tokenInfo.isDesigner()){
                if (orderId == null){
                    System.out.println("Ordersssss details found-designer");
                    OrderService service = new OrderService(resp);
                    service.getAllDesignerOrderDetails(userId);
                } else {
                    OrderService service = new OrderService(resp);
                    service.getOrderDetails(orderId);
                }

            } else if (tokenInfo.isClient()) {
                if (orderId == null){
                    System.out.println("Ordersssss details found-client");
                    OrderService service = new OrderService(resp);
                    service.getAllClientOrderDetails(userId);
                } else {
                    OrderService service = new OrderService(resp);
                    service.getOrderDetails(orderId);
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
        System.out.println(orderId);
        if (tokenService.isTokenValid(token)){

            if (tokenInfo.isClient()){

                Gson gson = new Gson();

                // creating an Order object using the request body
                BufferedReader reader = req.getReader();
                Order order = gson.fromJson(reader,Order.class);

                OrderService service = new OrderService(resp);

                int result1 = service.updateOrder(order);
                System.out.println(result1);

                if (result1 > 0){

                    double totalPrice = service.calTotalPayAmount(order);
//                    System.out.println(totalPrice);
                    Payment newPayment = new Payment(totalPrice, orderId);

                    //store the payment
                    PaymentService paymentService = new PaymentService();
                    int result2 = paymentService.savePaymentDetails(newPayment);

                    if (result2 > 0) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        out.write("Order Update successful");
                        System.out.println("Order Update successful");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        out.write("Order Update unsuccessful");
                        System.out.println("Order Update unsuccessful");
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write("Order Update unsuccessful");
                    System.out.println("Order Update unsuccessful");
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
