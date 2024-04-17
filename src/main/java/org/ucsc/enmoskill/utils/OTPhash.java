package org.ucsc.enmoskill.utils;

import java.util.Random;
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

}
