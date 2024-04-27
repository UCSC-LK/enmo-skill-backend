package org.ucsc.enmoskill.controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.Services.PaymentDetailsGET;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.utils.Payment_hashGen;
import org.ucsc.enmoskill.utils.TokenService;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

import static org.ucsc.enmoskill.utils.Payment_hashGen.getMd5;

public class PaymentControler extends HttpServlet {
    Dotenv dotenv = Dotenv.load();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);
        if (tokenService.isTokenValidState(token) == 1) {

            try {

                String merchantId = dotenv.get("MERCHANT_ID");
                System.out.println(merchantId);
                String orderId = req.getParameter("orderId");
                String amount = req.getParameter("amount");
                String currency = req.getParameter("currency");
                String merchantSecret = dotenv.get("MERCHANT_SECRET");
                System.out.println(merchantSecret);

                if(orderId == null || amount == null || currency == null){
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                    return;
                }
                if (merchantId == null || merchantSecret == null) {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }

                Payment_hashGen payment_hashGen = new Payment_hashGen(merchantId, orderId, Double.parseDouble(amount), currency, merchantSecret);
                String hash = payment_hashGen.createHash();
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(hash);

            }catch (Exception e){
                e.printStackTrace();
            }

        } else if (tokenService.isTokenValidState(token) == 2) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }




    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> paymentConfirmation = req.getParameterMap();
        String merchantId = req.getParameter("merchant_id");
        String orderId = req.getParameter("order_id");
        String paymentId = req.getParameter("payment_id");
        String amount = req.getParameter("payhere_amount");
        String currency = req.getParameter("payhere_currency");
        String statusCode = req.getParameter("status_code");
        String custom1 = req.getParameter("custom_1");
        String custom2 = req.getParameter("custom_2");
        String method = req.getParameter("method");
        String statusMessage = req.getParameter("status_message");


        String cardHolderName = req.getParameter("card_holder_name");
        String maskedCardNo = req.getParameter("card_no");
        String cardExpiry = req.getParameter("card_expiry");

        System.out.println("Payment Confirmation Received");
        System.out.println(merchantId+" "+orderId+" "+paymentId+" "+amount+" "+currency+" "+statusCode+" "+custom1+" "+custom2+" "+method+" "+statusMessage);
        Payment_hashGen payment_hashGen = new Payment_hashGen();

        if(payment_hashGen.validatePaymentConfirmation(paymentConfirmation)){
            System.out.println("Payment Confirmation is Valid");
            Connection connection = DatabaseConnection.initializeDatabase();
            String query = "UPDATE orders SET status = 1 WHERE order_id = ?";
            String query2 = "INSERT INTO payment (order_id, amount, status_code,  method, status_message) VALUES (?, ?, ?, ?, ?)";

            try {
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                PreparedStatement preparedStatement1 = connection.prepareStatement(query);
                preparedStatement1.setString(1, orderId);
                preparedStatement2.setString(1, orderId);
                preparedStatement2.setString(2, amount);
                preparedStatement2.setString(3, statusCode);
                preparedStatement2.setString(4, method);
                preparedStatement2.setString(5, statusMessage);
                connection.setAutoCommit(false);
                if (statusCode.equals("2")) {
                    preparedStatement1.executeUpdate();
                }
                preparedStatement2.executeUpdate();
                connection.commit();
                connection.setAutoCommit(true);
                System.out.println("Payment Confirmation is Valid");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Payment Confirmation is Invalid");
        }



    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderid = req.getParameter("orderId");
        if (orderid == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);
        if (tokenService.isTokenValidState(token) == 1) {
            PaymentDetailsGET paymentDetailsGET = new PaymentDetailsGET(resp, tokenService.getTokenInfo(token), orderid);
            paymentDetailsGET.Run();

        } else if (tokenService.isTokenValidState(token) == 2) {
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
