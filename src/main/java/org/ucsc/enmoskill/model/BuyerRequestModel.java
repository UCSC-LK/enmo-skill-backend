package org.ucsc.enmoskill.model;

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

}
