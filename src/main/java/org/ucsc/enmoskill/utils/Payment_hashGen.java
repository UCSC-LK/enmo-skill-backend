package org.ucsc.enmoskill.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Map;

public class Payment_hashGen {
    String merchantId, orderId, currency, merchantSecret;
    double amount;
    Dotenv dotenv = Dotenv.load();
    public Payment_hashGen() {

    }
    public Payment_hashGen(String merchantId, String orderId, Double amount, String currency, String merchantSecret) {
        this.merchantId = merchantId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.merchantSecret = merchantSecret;
    }

    //create hash using md5 algorithm
    public String createHash() {
        DecimalFormat df       = new DecimalFormat("0.00");
        String amountFormatted = df.format(amount);
        String hash    = getMd5(merchantId + orderId + amountFormatted + currency + getMd5(merchantSecret));
        System.out.println("Generated Hash: " + hash);
        return hash;
    }

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.toUpperCase();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean validatePaymentConfirmation(Map<String, String[]> paymentConfirmation){
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
