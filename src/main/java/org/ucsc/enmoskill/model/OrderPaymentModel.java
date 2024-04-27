package org.ucsc.enmoskill.model;

import java.sql.ResultSet;

public class OrderPaymentModel {
    private String orderId;
    private String client_userID;
    private String title;
    private String cover_url;
    private String type;
    private double total_price;
    private double package_price;
    private double usercharge;

    private int status;

    public OrderPaymentModel(String orderId, String client_userID, String title, String cover_url, String type, double total_price, double package_price, double usercharge, int status) {
        this.orderId = orderId;
        this.client_userID = client_userID;
        this.title = title;
        this.cover_url = cover_url;
        this.type = type;
        this.total_price = total_price;
        this.package_price = package_price;
        this.usercharge = usercharge;
        this.status = status;
    }
    public OrderPaymentModel(ResultSet resultSet){
        try {
            this.orderId = resultSet.getString("order_id");
            this.client_userID = resultSet.getString("client_userID");
            this.title = resultSet.getString("title");
            this.cover_url = resultSet.getString("cover_url");
            this.type = resultSet.getString("type");
            this.total_price = resultSet.getDouble("total_price");
            this.package_price = resultSet.getDouble("package_price");
            this.usercharge = resultSet.getDouble("usercharge");
            this.status = resultSet.getInt("status");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClient_userID() {
        return client_userID;
    }

    public void setClient_userID(String client_userID) {
        this.client_userID = client_userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getPackage_price() {
        return package_price;
    }

    public void setPackage_price(double package_price) {
        this.package_price = package_price;
    }

    public double getUsercharge() {
        return usercharge;
    }

    public void setUsercharge(double usercharge) {
        this.usercharge = usercharge;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
