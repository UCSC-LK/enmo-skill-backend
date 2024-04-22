package org.ucsc.enmoskill.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ErningsModel {
    private  double available,begin,active,all,amount,lastAmount;
    private int status,orderId,packegeId;
    private String date,lastDate;


    public ErningsModel(double available, double begin, double active, double all,double amount,double lastAmount,int status, int orderId, int packegeId, String date,String lastDate) {
        this.available = available;
        this.begin = begin;
        this.active = active;
        this.all = all;
        this.status = status;
        this.orderId = orderId;
        this.packegeId = packegeId;
        this.date = date;
        this.amount=amount;
        this.lastAmount=lastAmount;
        this.lastDate=lastDate;
    }

    public ErningsModel(double available, double active, double begin,double all,double lastAmount,String lastDate) throws SQLException {
;
        this.begin=begin;
        this.active=active;
        this.all=available;
        this.available=available-all;
        this.lastDate=lastDate;
        this.lastAmount=lastAmount;


    }

    public ErningsModel(ResultSet result) throws SQLException {
        this.orderId=result.getInt("order_id");
        this.status=result.getInt("status");
        this.packegeId=result.getInt("package_id");
        this.date=result.getString("created_time");
        this.amount=result.getDouble("price");
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getBegin() {
        return begin;
    }

    public void setBegin(double begin) {
        this.begin = begin;
    }

    public double getActive() {
        return active;
    }

    public void setActive(double active) {
        this.active = active;
    }

    public double getAll() {
        return all;
    }

    public void setAll(double all) {
        this.all = all;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPackegeId() {
        return packegeId;
    }

    public void setPackegeId(int packegeId) {
        this.packegeId = packegeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(double lastAmount) {
        this.lastAmount = lastAmount;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }
}
