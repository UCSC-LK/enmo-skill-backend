package org.ucsc.enmoskill.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.ucsc.enmoskill.database.DatabaseConnection;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ForgetPassword {
    private static final Key SIGNING_KEY;
    private final long EXPIRATION_TIME_THREE_HOUR = 10800000;

    static {
        Dotenv dotenv = Dotenv.load();
        String signingKeyString = dotenv.get("FORGET_SIGNING_KEY");
        SIGNING_KEY = new SecretKeySpec(signingKeyString.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }
    public  String generateToken(String email) {
        return  Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_THREE_HOUR))
                .signWith(SIGNING_KEY)
                .compact();
    }
    public  boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getInfo(String token) {
        Claims claims = Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token).getBody();
        String email = claims.getSubject();
        return email;
    }

    public boolean updatePassword(String email, String password) {
        String hashedPassword = Hash.hashPassword(password);
        System.out.println(hashedPassword);
        Connection connection = DatabaseConnection.initializeDatabase();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET password = ? WHERE email = ?");
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);
            int executed = preparedStatement.executeUpdate();
            if (executed == 0) {
                return false;
            }else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean sendEmail(String email, String token) throws IOException {

        Dotenv dotenv = Dotenv.load();
        String apiUrl = "https://api.elasticemail.com/v2/email/send";
        String apiKey = "31002F15EC8BDD202DD460E2A234049861A1467A2791552593B041F0A05AED5AD6410287273B6B0A911AF32FE5C7946E";
        String subject = "Reset Your Account Password";
        String from = "enmoskill@mail.com";
        String to =email;
        String fromName = "Enmo Skill Platform";
        String template = "reset password";
        String mergeCode = dotenv.get("BASE_URL")+"/HTML/resetPassword.html?token="+token;



        String requestBody = "apikey=" + apiKey +
                "&subject=" + subject +
                "&from=" + from +
                "&to=" + to +
                "&fromName=" + fromName +
                "&template=" + template +
                "&merge_code=" + mergeCode;


        URL urlObject = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        int responseCode = connection.getResponseCode();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String line;
        StringBuilder responses = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            responses.append(line);
        }
        reader.close();
        JsonObject jsonResponse = JsonParser.parseString(responses.toString()).getAsJsonObject();
        if (responseCode==200){

            System.out.println(jsonResponse);
            boolean success = jsonResponse.getAsJsonPrimitive("success").getAsBoolean();
            if (success) {
                System.out.println("Request successful");
                return true;

            } else {
                return false;
//                    System.out.println("Request failed");

            }}

        connection.disconnect();
        return false;
    }

    public boolean isEmailExists(String email) {
        Connection connection = DatabaseConnection.initializeDatabase();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
