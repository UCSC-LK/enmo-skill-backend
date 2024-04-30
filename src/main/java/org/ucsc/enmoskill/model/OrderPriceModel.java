package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderPriceModel {
    private String packegePrice;
    private double siteChage,total;

    public OrderPriceModel(String packegePrice, double siteChage, double total) {
        this.packegePrice = packegePrice;
        this.siteChage = siteChage;
        this.total = total;
    }

    public OrderPriceModel(ResultSet result) throws SQLException {
        this.packegePrice=result.getString("price");
        double price = Double.parseDouble((this.packegePrice));
        this.siteChage= price*0.1;
        this.total=this.siteChage+price;
    }


    public String getPackegePrice() {
        return packegePrice;
    }

    public void setPackegePrice(String packegePrice) {
        this.packegePrice = packegePrice;
    }

    public double getSiteChage() {
        return siteChage;
    }

    public void setSiteChage(double siteChage) {
        this.siteChage = siteChage;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
