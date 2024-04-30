package org.ucsc.enmoskill.Services;

import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.BuyerRequestModel;
import org.ucsc.enmoskill.model.MessageModel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class MessagesPOST {
    private HttpServletResponse response;
    private MessageModel data;

    public MessagesPOST(HttpServletResponse response, MessageModel data) {
        this.response = response;
        this.data = data;
    }

    public void Run() throws IOException {
        Connection connection = DatabaseConnection.initializeDatabase();
        if(connection==null){
            response.getWriter().write("SQL Connection Error");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }



        try {

            String chatCheckQuery = "SELECT * FROM chat WHERE chat_id = "+data.getChatid();
            PreparedStatement chatCheckStatement = connection.prepareStatement(chatCheckQuery);
            ResultSet chatCheckResult = chatCheckStatement.executeQuery();
            if (chatCheckResult.next()) {
                if (chatCheckResult.getInt("user_1") != Integer.parseInt(data.getUser()) && chatCheckResult.getInt("user_2") != Integer.parseInt(data.getUser())) {
                    response.getWriter().write("User not in chat");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

            }



            String massageQuery = "INSERT INTO massage (content, time, date) VALUES (?, UNIX_TIMESTAMP() * 1000, CURDATE())";
            String chatMappingQuery = "INSERT INTO chat_mapping (chat_id, msg_id, receiver) VALUES (?, ?, ?)";

            PreparedStatement massageStatement = connection.prepareStatement(massageQuery, Statement.RETURN_GENERATED_KEYS);
            massageStatement.setString(1, data.getMessage());

            int affectedRows = massageStatement.executeUpdate();

            if (affectedRows == 0) {
                response.getWriter().write("Inserting message failed, no rows affected.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                throw new SQLException("Inserting message failed, no rows affected.");
            }

            try (ResultSet generatedKeys = massageStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long lastMsgId = generatedKeys.getLong(1);

                    PreparedStatement chatMappingStatement = connection.prepareStatement(chatMappingQuery);
                    chatMappingStatement.setInt(1, Integer.parseInt(data.getChatid()));
                    chatMappingStatement.setLong(2, lastMsgId);
                    chatMappingStatement.setInt(3, Integer.parseInt(data.getUser()));

                    int chatMappingRowsAffected = chatMappingStatement.executeUpdate();
                    if (chatMappingRowsAffected == 0) {
                        response.getWriter().write("Inserting chat mapping failed, no rows affected.");
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        throw new SQLException("Inserting chat mapping failed, no rows affected.");

                    }
                    else {
                        response.getWriter().write("message sent");
                        response.setStatus(HttpServletResponse.SC_CREATED);
                    }
                } else {
                    response.getWriter().write("Inserting message failed, no ID obtained.");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    throw new SQLException("Inserting message failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }



    }
}
