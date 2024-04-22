package org.ucsc.enmoskill.Services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SendEmail {
    private String email, subject, username,ticketId,userId;


    public SendEmail(String ticketId, String userId, String email, String subject, String username) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.email = email;
        this.subject = subject;
        this.username = username;
    }

    public SendEmail(String ticketId,String userId){
        this.ticketId = ticketId;
        this.userId = userId;
    }


    public void setdata(String type) throws SQLException, IOException {
        PreparedStatement preparedStatement;
        PreparedStatement preparedStatement1;
        Connection initializeDatabase = DatabaseConnection.initializeDatabase();

        String apiKey = "31002F15EC8BDD202DD460E2A234049861A1467A2791552593B041F0A05AED5AD6410287273B6B0A911AF32FE5C7946E";


        preparedStatement1 = initializeDatabase.prepareStatement("SELECT t.subject,t.requesterID FROM enmo_database.ticket t WHERE t.ref_no=\'" + this.ticketId + "\'");
        ResultSet resultSet1 = preparedStatement1.executeQuery();

        preparedStatement = initializeDatabase.prepareStatement("SELECT t.email,t.username FROM enmo_database.users t WHERE userID=\'" + this.userId + "\'");
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet1.next()) {
            this.subject = resultSet1.getString("subject");
        }
        if (resultSet.next()) {
            this.email = resultSet.getString("email");
            this.username = resultSet.getString("username");
        }

        String subject=null;
        String template=null;

        if (type == "agent") {
             subject="New Support Ticket Assigned to You";
            template="notification email-agent";

        }else if (type == "reply") {
            subject="Your support ticket has updated";
            template="Notify updates";
        }else if(type=="reject"){
            subject="your Support Ticket was rejected";
            template="rejected ticket";
        }else if(type=="close"){
            subject="your Support Ticket was closed";
            template="close ticket";
        }

        String requestBody = "apikey=" + apiKey +
                "&subject="+subject +
                "&from=" + "enmoskill@mail.com" +
                "&to=" + this.email +
                "&fromName=Enmo Skill Platform" +
                "&template="+template +
                "&merge_username=" + this.username +
                "&merge_ticketId=" + this.ticketId +
                "&merge_ticketSubject="+this.subject;
        send(requestBody);

    }

    public ResponsModel send(String requestBody) throws IOException {

        String apiUrl = "https://api.elasticemail.com/v2/email/send";

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
        System.out.println(responses);
        if (responseCode == 200) {

            boolean success = jsonResponse.getAsJsonPrimitive("success").getAsBoolean();
            if (success) {
                return new ResponsModel("Successfully Sent", HttpServletResponse.SC_OK);
            } else {
                return new ResponsModel("Failed to Send! System Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            }
        }else{
                return new ResponsModel("Connection Error",HttpServletResponse.SC_NOT_FOUND);
        }

    }
}
