package org.ucsc.enmoskill.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyerRequestModel {

    private int requestID ,duration , budget , userID,status;
    private String date , discription,username;

    public BuyerRequestModel(int requestID, int duration, int budget, int userID, int status, String date, String discription ,String username) {
        this.requestID = requestID;
        this.duration = duration;
        this.budget = budget;
        this.userID = userID;
        this.status = status;
        this.date = date;
        this.discription = discription;
        this.username=username;
    }
// Constructors, getters, and setters

    public BuyerRequestModel(ResultSet resultSet) throws SQLException {
        this.requestID = resultSet.getInt("requestID");
        this.duration = resultSet.getInt("duration");
        this.date = resultSet.getString("date");
        this.discription = resultSet.getString("discription");
        this.budget = resultSet.getInt("budget");
        this.userID = resultSet.getInt("userID");
        this.status = resultSet.getInt("status");
        this.username = resultSet.getString("username");

    }

    public String getQuery(String Type){
        Date Today= new Date();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);
        if(Type.equals("insert")){
            String quary ="INSERT INTO buyer_request (userID, date, discription, duration, budget, status) VALUES ("+this.userID+", \'"+Date+"\', \'"+this.discription+"\', "+this.duration+", "+this.budget+", 1)" ;
            return quary;
        }
        return null;
    }



    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
