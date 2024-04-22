package org.ucsc.enmoskill.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyerRequestModel {

    private int requestID ,duration , budget , userID,status;
    private String date , discription,username,sample_work_url,title;

    public BuyerRequestModel(int requestID, int duration, int budget, int userID, int status, String date, String discription ,String username,String sample_work_url,String title) {
        this.requestID = requestID;
        this.title=title;
        this.duration = duration;
        this.budget = budget;
        this.userID = userID;
        this.status = status;
        this.date = date;
        this.discription = discription;
        this.username=username;
        this.sample_work_url=sample_work_url;
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
        this.sample_work_url=resultSet.getString("sample_work_url");
        this.title=resultSet.getString("title");

    }

    public String getQuery(String Type){
        Date Today= new Date();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);
        if(Type.equals("insert")){
            String quary ="INSERT INTO jobs (userID,title, date, discription, duration, budget, status,sample_work_url) VALUES ("+this.userID+",\'"+this.title+"\', \'"+Date+"\', \'"+this.discription+"\', "+this.duration+", "+this.budget+", 1,\'"+this.sample_work_url+"\')" ;
            return quary;
        }
        if(Type.equals("update")){
            String quary =" UPDATE enmo_database.jobs t SET t.title = '"+this.title+"',  t.discription = '"+this.discription+"', t.duration = "+this.duration+",t.budget ="+this.budget+", t.sample_work_url=\'"+this.sample_work_url+"\' WHERE t.requestID ="+this.requestID +" AND t.userID ="+this.userID;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSample_work_url() {
        return sample_work_url;
    }

    public void setSample_work_url(String sample_work_url) {
        this.sample_work_url = sample_work_url;
    }
}
