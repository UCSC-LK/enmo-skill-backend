package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BillingInformationModel {
    private String fname,lname,pNumber,email,address,country,city;

    public BillingInformationModel(String fname, String lname, String pNumber, String email, String address, String country, String city) {
        this.fname = fname;
        this.lname = lname;
        this.pNumber = pNumber;
        this.email = email;
        this.address = address;
        this.country = country;
        this.city = city;
    }

    public BillingInformationModel(ResultSet resultSet,String update) throws SQLException {
        this.email=resultSet.getString("email");
        this.pNumber=resultSet.getString("contact_no");
    }

    public BillingInformationModel(ResultSet result) throws SQLException {
        this.fname=result.getString("fname");
        this.lname=result.getString("lname");
        this.pNumber=result.getString("phoneNo");
        this.email=result.getString("email");
        this.address=result.getString("address");
        this.city=result.getString("city");
        this.country=result.getString("country");
    }


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
