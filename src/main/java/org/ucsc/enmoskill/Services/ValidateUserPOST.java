package org.ucsc.enmoskill.Services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ucsc.enmoskill.database.DatabaseConnection;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;

public class ValidateUserPOST {
    private HttpServletResponse response;
    private String key;
    boolean falgConstructor=false;
    public ValidateUserPOST( HttpServletResponse response,String key){
        this.key=key;
        this.response=response;
    }
    public ValidateUserPOST( HttpServletResponse response){
        this.response=response;
    }
    public  ValidateUserPOST(){
//        falgConstructor=true;
    }
    public String storeKey(int UserID) throws SQLException, IOException {
        String generatedString = RandomStringUtils.randomAlphanumeric(15);
        Connection connection = DatabaseConnection.initializeDatabase();
        Date Today= new Date();
        long time = Today.getTime();
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("UPDATE enmo_database.user_verify t SET t.key = \'"+generatedString+"\' ,t.time ="+time+" WHERE t.userid = "+UserID);
//        preparedStatement.setString(1,generatedString);
//        preparedStatement.setInt(2,UserID);
        int RowAffected = preparedStatement.executeUpdate();
        if (RowAffected>0)
        {
//            if(!falgConstructor){
//                response.setStatus(HttpServletResponse.SC_CREATED);
//                response.getWriter().write("Successfully Created!");
//            }
            return generatedString;
        }

        return null;
    }

    public boolean Validate() {
        PreparedStatement preparedStatement;
        String query ="SELECT t.* FROM enmo_database.user_verify t WHERE `key`=\'"+this.key+"\'";
        Connection connection = DatabaseConnection.initializeDatabase();
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int UserId = resultSet.getInt("userid");
                long time = resultSet.getLong("time");

                Date Today= new Date();
                long nowtime = Today.getTime();

//                System.out.println("now :"+nowtime+"  created :"+time);

                if(nowtime-time>86400000){
                    response.getWriter().write("Link Expired");
                    response.setStatus(HttpServletResponse.SC_GONE);
                    return true;
                }

                preparedStatement = connection.prepareStatement("SELECT t.* FROM enmo_database.users t WHERE userID ="+UserId);
                ResultSet resultSet2 = preparedStatement.executeQuery();
                if (resultSet2.next()) {
                    int status = resultSet2.getInt("status");
                    System.out.println(status);
                    if(status==0){
                        preparedStatement = connection.prepareStatement("UPDATE enmo_database.users t SET t.status = 1 WHERE t.userID ="+UserId);
                        int RowAffected = preparedStatement.executeUpdate();
                        if (RowAffected>0){
                            response.setStatus(HttpServletResponse.SC_OK);
                            JsonObject jsonObject =new JsonObject();
                            jsonObject.addProperty("UserId",UserId);
                            response.getWriter().write(jsonObject.toString());
                            return true;
                        }
                    }
                    else if(status==1) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        response.getWriter().write("Email has already been verified");
                        return true;
                    }
                }

            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean resend(){
        PreparedStatement preparedStatement;
        String query ="SELECT t.* FROM enmo_database.user_verify t WHERE `key`=\'"+this.key+"\'";
        Connection connection = DatabaseConnection.initializeDatabase();
        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int UserId = resultSet.getInt("userid");

                preparedStatement = connection.prepareStatement("SELECT t.* FROM enmo_database.users t WHERE userID ="+UserId);
                ResultSet resultSet2 = preparedStatement.executeQuery();
                if (resultSet2.next()) {
                    String email = resultSet2.getString("email");
                    String username = resultSet2.getString("username");



                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }return false;
    }

    public void send(String email){
        PreparedStatement preparedStatement;
        Connection initializeDatabase = DatabaseConnection.initializeDatabase();
        try {
            preparedStatement = initializeDatabase.prepareStatement("SELECT t.* FROM enmo_database.users t WHERE email=\'"+email+"\'");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int UserId = resultSet.getInt("userid");
                String Username = resultSet.getString("username");

                String code =storeKey(UserId);
                String apiUrl = "https://api.elasticemail.com/v2/email/send";
                String apiKey = "31002F15EC8BDD202DD460E2A234049861A1467A2791552593B041F0A05AED5AD6410287273B6B0A911AF32FE5C7946E";
                String subject = "Email Address Verification";
                String from = "enmoskill@mail.com";
                String to =email;
                String fromName = "Enmo Skill Platform";
                String template = "email validate";
                String mergeCode = "https://enmoskill.codingblinders.com/HTML/validateEmail.html?key="+code;
                String mergeUsername = Username;


                String requestBody = "apikey=" + apiKey +
                        "&subject=" + subject +
                        "&from=" + from +
                        "&to=" + to +
                        "&fromName=" + fromName +
                        "&template=" + template +
                        "&merge_code=" + mergeCode +
                        "&merge_username=" + mergeUsername;


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
                boolean success = jsonResponse.getAsJsonPrimitive("success").getAsBoolean();
                if (success) {
                    response.getWriter().write("Successfully Sent");
                    response.setStatus(HttpServletResponse.SC_OK);
//                    System.out.println("Request was successful");
                } else {
//                    System.out.println("Request failed");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Failed to Send! System Error");
                }}

                connection.disconnect();
            }
            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid Email");
//                System.out.println("email is wrong or not written in database");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
