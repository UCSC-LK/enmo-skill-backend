package org.ucsc.enmoskill.model;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private Timestamp createdTime;
    private String requirements;
    private int status;
    private int designerId;
    private int clientId;
    private int packageId;
    private int price;
    private int platformFeeId;

    public Order(){}

    public Order(int orderId, Timestamp createdTime, String requirements, int status, int designerId, int clientId, int packageId, int price, int platformFeeId){
        this.orderId = orderId;
        this.createdTime = createdTime;
        this.requirements = requirements;
        this.status = status;
        this.designerId = designerId;
        this.clientId = clientId;
        this.packageId = packageId;
        this.price = price;
        this.platformFeeId = platformFeeId;
    }

    public Order(int orderId, String requirements, int status, int designerId, int clientId, int packageId, int price, int platformFeeId){
        this.orderId = orderId;
        this.requirements = requirements;
        this.status = status;
        this.designerId = designerId;
        this.clientId = clientId;
        this.packageId = packageId;
        this.price = price;
        this.platformFeeId = platformFeeId;
    }

    public int getPackageId() {
        return packageId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getDesignerId() {
        return designerId;
    }

    public int getPlatformFeeId() {
        return platformFeeId;
    }

    public int getPrice() {
        return price;
    }

    public int getStatus() {
        return status;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public void setDesignerId(int designerId) {
        this.designerId = designerId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setPlatformFeeId(int platformFeeId) {
        this.platformFeeId = platformFeeId;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }


}
