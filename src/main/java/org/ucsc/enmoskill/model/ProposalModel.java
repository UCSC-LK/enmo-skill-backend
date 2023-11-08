package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProposalModel {

    private String proposalID ,duration ,budget,userID , requestID;
    private String date, description ;

    public ProposalModel(String proposalID, String duration, String budget , String userID, String date, String description ,String requestID){
        this.proposalID = proposalID;
        this.duration = duration;
        this.budget = budget;
        this.userID = userID;
        this.date = date;
        this.description =description;
        this.requestID = requestID;
    }

    public ProposalModel(ResultSet resultSet) throws SQLException {
        this.proposalID = resultSet.getString("proposalID");
        this.duration = resultSet.getString("duration");
        this.budget = resultSet.getString("budget");
        this.userID = resultSet.getString("userID");
        this.date = resultSet.getString("date");
        this.description = resultSet.getString("description");
        this.requestID = resultSet.getString("requestID");
    }

    public String getProposalID() {
        return proposalID;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestid() {
        return requestID;
    }

    public void setRequestid(String requestid) {
        this.requestID = requestid;
    }
}
