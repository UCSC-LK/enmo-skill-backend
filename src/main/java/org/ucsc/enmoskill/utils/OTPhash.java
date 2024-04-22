package org.ucsc.enmoskill.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OTPhash {

    public static String OTPHashGen(String input){
        try
        {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32)
            {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String randomNumGen(){

        int min=100000;
        int max = 1000000;

        String ranNumber= String.valueOf(min + (int)(Math.random() * ((max - min) + 1)));
        System.out.println(ranNumber);

        return ranNumber;

    }

    public boolean checkOTP(String otp,String hashString){

        String newhash = OTPHashGen(otp);

        System.out.println(newhash);
        System.out.println(hashString);

        if(newhash.equals(hashString)) {
            return true;
        }else{
            return false;
        }
    }

    public boolean sendOTP(String number, String otp) throws IOException {

        String massage = "Code "+otp+" is your One Time Password to authorize the transaction. Do NOT share this number with anyone";

        String apiUrl = "https://www.textit.biz/sendmsg?id=94716676968&pw=8241&to="+number+"&text="+ URLEncoder.encode(massage, StandardCharsets.UTF_8);;

        URL urlObject = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line;
        StringBuilder responses = new StringBuilder();


        while ((line = reader.readLine()) != null) {
            responses.append(line);
        }

        reader.close();

        System.out.println(responses);
        if (responses.toString().contains("Uploaded_Successfully")) {
            return true;
        }else{
            return false;
        }

    }

}
