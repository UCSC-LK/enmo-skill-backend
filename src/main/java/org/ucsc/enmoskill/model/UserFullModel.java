package org.ucsc.enmoskill.model;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.Date;

    public class UserFullModel {
    private User user;
    private String fname;
    private String lname;

    private String joinedDate;

    private int status;

    public UserFullModel(){}

    public UserFullModel(User user, String fname, String lname, String joinedDate, int status){
        this.user = user;
        this.joinedDate = joinedDate;
        this.status = status;
        this.fname = fname;
        this.lname = lname;
    }

    public int getStatus() {
        return status;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public User getUser() {
        return user;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
