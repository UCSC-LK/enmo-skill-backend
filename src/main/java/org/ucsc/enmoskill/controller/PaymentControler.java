package org.ucsc.enmoskill.controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.utils.Payment_hashGen;
import org.ucsc.enmoskill.utils.TokenService;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        // If payment made by VISA or MASTER card, retrieve additional parameters
        String cardHolderName = req.getParameter("card_holder_name");
        String maskedCardNo = req.getParameter("card_no");
        String cardExpiry = req.getParameter("card_expiry");

        System.out.println("Payment Confirmation Received");
        System.out.println(merchantId+" "+orderId+" "+paymentId+" "+amount+" "+currency+" "+statusCode+" "+custom1+" "+custom2+" "+method+" "+statusMessage);

        if(validatePaymentConfirmation(paymentConfirmation)){
            System.out.println("Payment Confirmation is Valid");
        }
        else{
            System.out.println("Payment Confirmation is Invalid");
        }



    }
    private boolean validatePaymentConfirmation(Map<String, String[]> paymentConfirmation){
        if(paymentConfirmation.get("md5sig")[0]==null){
            return false;
        }
        String merchantsSecret = dotenv.get("MERCHANT_SECRET");
        if(paymentConfirmation.get("md5sig")[0].equals(generateMd5sig(paymentConfirmation, merchantsSecret))){
            return true;
        }
        else{
            return false;
        }
    }
    public static String generateMd5sig(Map<String, String[]> paymentConfirmation, String merchantSecret){
        String checksum = null;
        String md5Hash = getMd5(merchantSecret);
//        String concatString = merchantId + orderId + payhereAmount + payhereCurrency + statusCode + md5Hash;
        String concatString = paymentConfirmation.get("merchant_id")[0] + paymentConfirmation.get("order_id")[0] + paymentConfirmation.get("payhere_amount")[0] + paymentConfirmation.get("payhere_currency")[0] + paymentConfirmation.get("status_code")[0] + md5Hash;
        checksum = getMd5(concatString);
        return checksum;
    }



}
