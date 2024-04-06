package org.ucsc.enmoskill.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class Payment_hashGen {
    String merchantId, orderId, currency, merchantSecret;
    double amount;

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
}
