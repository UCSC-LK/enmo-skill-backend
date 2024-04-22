package org.ucsc.enmoskill.Services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BannModel;
import org.ucsc.enmoskill.model.WarningModel;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BannService {


    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    int result = 0;


    public int insertBann(BannModel bann) {

        try {
            con = DatabaseConnection.initializeDatabase();
            String query = "INSERT INTO bann (reason, user_id, ticket_id) VALUES (?, ?, ?);";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, bann.getReason());
            preparedStatement.setInt(2, bann.getUserId());
            preparedStatement.setInt(3, bann.getTicketId());
            result = preparedStatement.executeUpdate();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error executing query: " + e.getMessage());
            throw new RuntimeException("Failed to insert data", e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }


    }

    public void send(int userId, HttpServletResponse response){
        PreparedStatement preparedStatement;
        Connection initializeDatabase = DatabaseConnection.initializeDatabase();
        try {
            preparedStatement = initializeDatabase.prepareStatement("SELECT t.* FROM enmo_database.users t WHERE userID="+userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int UserId = resultSet.getInt("userid");
                String Username = resultSet.getString("username");
                String email = resultSet.getString("email");

//                String code =storeKey(UserId);
                String apiUrl = "https://api.elasticemail.com/v2/email/send";
                String apiKey = "31002F15EC8BDD202DD460E2A234049861A1467A2791552593B041F0A05AED5AD6410287273B6B0A911AF32FE5C7946E";
                String subject = "Account suspended";
                String from = "enmoskill@mail.com";
                String to =email;
                String fromName = "Enmo Skill Platform";
                String template = "bann_template";
//                String mergeCode = "http://127.0.0.1:5501/HTML/validateEmail.html?key="+code;
                String mergeUsername = Username;


                String requestBody = "apikey=" + apiKey +
                        "&subject=" + subject +
                        "&from=" + from +
                        "&to=" + to +
                        "&fromName=" + fromName +
                        "&template=" + template +
//                        "&merge_code=" + mergeCode +
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
