package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageModel {
    String Message,time,user,chatid;

    public MessageModel(String message, String time, String user, String chatid) {
        Message = message;
        this.time = time;
        this.user = user;
        this.chatid = chatid;
    }

    public  MessageModel(ResultSet resultSet) throws SQLException {
        this.Message = resultSet.getString("content");
        this.time = resultSet.getString("time");
        this.user = resultSet.getString("receiver_id");

    }
    public String getQuery(String type){
         if(type.equals("message")){

         }
        return null;
    }

    public int isYours(String userid){
        if (userid.equals(user)){
            return 1;
        }else return 0;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }
}
