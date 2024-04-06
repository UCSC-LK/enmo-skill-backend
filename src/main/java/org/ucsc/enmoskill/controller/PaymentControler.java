package org.ucsc.enmoskill.controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.ucsc.enmoskill.utils.Payment_hashGen;
import org.ucsc.enmoskill.utils.TokenService;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class PaymentControler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        TokenService tokenService = new TokenService();
        String token = tokenService.getTokenFromHeader(req);
        if (tokenService.isTokenValidState(token) == 1) {

            try {
                Dotenv dotenv = Dotenv.load();
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
}
