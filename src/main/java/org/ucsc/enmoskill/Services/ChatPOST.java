package org.ucsc.enmoskill.Services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.ucsc.enmoskill.database.DatabaseConnection;
import org.ucsc.enmoskill.model.ResponsModel;
import org.ucsc.enmoskill.model.User;

import java.sql.*;

public class ChatPOST {

    String receiverID;
    String userId;
    public ChatPOST(String receiverID, String userId) {
        this.receiverID = receiverID;
        this.userId = userId;
    }
    public ResponsModel Run() {


        String query = "INSERT INTO enmo_database.chat (user_1, user_2) VALUES (?, ?)";
        Connection connection = DatabaseConnection.initializeDatabase();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, receiverID);
            int affected = preparedStatement.executeUpdate();
            if (affected == 0) {
                System.out.println("Chat creation failed, no rows affected.");
                ResponsModel responsModel = new ResponsModel("Chat creation failed, no rows affected.", 500);
                responsModel.setState(false);
                return responsModel;

            }else{
                System.out.println("Chat created successfully");
                try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        System.out.println("Chat id: "+generatedKeys.getInt(1));
                        return sendtoUser(generatedKeys.getInt(1));

                    }else{
                        throw new SQLException("Creating chat failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            // This block will execute if there is a duplicate entry
            String fetchQuery = "SELECT chat_id FROM enmo_database.chat WHERE user_1 = ? AND user_2 = ?";
            PreparedStatement fetchStatement = null;
            try {
                fetchStatement = connection.prepareStatement(fetchQuery);
                fetchStatement.setString(1, userId);
                fetchStatement.setString(2, receiverID);
                ResultSet resultSet = fetchStatement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("Existing chat id: " + resultSet.getInt("chat_id"));
                    return sendtoUser( resultSet.getInt("chat_id"));

                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private ResponsModel sendtoUser(int chatId){
        ResponsModel res = new UserGet(receiverID).Run();
        Gson gson = new Gson();
        User user = gson.fromJson(res.getResMassage(), User.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("chat_id", chatId);
        jsonObject.addProperty("username", user.getUsername());
        jsonObject.addProperty("url", user.getUrl());



        ResponsModel responsModel = new ResponsModel(jsonObject.toString(), 201);
        responsModel.setState(true);
        return responsModel;
    }

}
