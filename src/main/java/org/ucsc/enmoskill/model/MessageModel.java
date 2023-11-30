package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageModel {
    String Message,time,user;

    public MessageModel(String message, String time, String user) {
        Message = message;
        this.time = time;
        this.user = user;
    }
    public  MessageModel(ResultSet resultSet) throws SQLException {
        this.Message = resultSet.getString("content");
        this.time = resultSet.getString("time");
        this.user = resultSet.getString("receiver_id");

    }
    public int isYours(String userid){
        if (userid.equals(user)){
            return 1;
        }else return 0;
    }

    public String getMessage() {
        return Message;
    }

    public String getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }
}
