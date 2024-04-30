package org.ucsc.enmoskill.model;

import java.sql.Date;
import java.sql.ResultSet;

public class NotificationModel {
    private int notificationID;
    private int userid;
    private String content;
    private String type;
    private int status;
    private Date date;

    public NotificationModel(int notificationID, int userid, String content, String type, int status, Date date) {
        this.notificationID = notificationID;
        this.userid = userid;
        this.content = content;
        this.type = type;
        this.status = status;
        this.date = date;
    }
    public NotificationModel(ResultSet resultSet){
        try {
            this.notificationID = resultSet.getInt("notificationID");
            this.userid = resultSet.getInt("userId");
            this.content = resultSet.getString("content");
            this.type = resultSet.getString("type");
            this.status = resultSet.getInt("status");
            this.date = resultSet.getDate("date");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
