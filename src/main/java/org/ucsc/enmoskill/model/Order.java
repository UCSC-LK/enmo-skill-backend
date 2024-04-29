package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private int pricePackageId;

    private int deliveryDuration;

    private int proposalID;


    public Order(){}

    public Order(int orderId, Timestamp createdTime, String requirements, int status, int designerId, int clientId, int packageId, int price, int platformFeeId, int proposalID, int deliveryDuration , int pricePackageId){
        this.orderId = orderId;
        this.createdTime = createdTime;
        this.requirements = requirements;
        this.status = status;
        this.designerId = designerId;
        this.clientId = clientId;
        this.packageId = packageId;
        this.price = price;
        this.platformFeeId = platformFeeId;
        this.deliveryDuration = deliveryDuration;
        this.proposalID = proposalID;
        this.pricePackageId = pricePackageId;


        // Parse and set createdTime
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy, hh:mm:ss a");
        try {
            Date parsedDate = dateFormat.parse(String.valueOf(createdTime));
            this.createdTime = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing exception here
        }
    }

//    public Order(int orderId, String requirements, int status, int designerId, int clientId, int packageId, int price, int platformFeeId){
//        this.orderId = orderId;
//        this.requirements = requirements;
//        this.status = status;
//        this.designerId = designerId;
//        this.clientId = clientId;
//        this.packageId = packageId;
//        this.price = price;
//        this.platformFeeId = platformFeeId;
//    }

    public Order(ResultSet resultSet) throws SQLException {
        this.orderId = resultSet.getInt("order_id");
        this.createdTime = resultSet.getTimestamp("created_time");
        this.requirements = resultSet.getString("requirements");
        this.status = resultSet.getInt("status");
        this.designerId = resultSet.getInt("designer_userID");
        this.clientId = resultSet.getInt("client_userID");
        this.packageId = resultSet.getInt("package_id");
        this.price = resultSet.getInt("price");
        this.platformFeeId = resultSet.getInt("platform_fee_id");
        this.proposalID = resultSet.getInt("proposalID");
        this.deliveryDuration = resultSet.getInt("deliveryDuration");
        this.pricePackageId = resultSet.getInt("price_package_id");
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public int getProposalID() {
        return proposalID;
    }

    public void setProposalID(int proposalID) {
        this.proposalID = proposalID;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
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

    public Timestamp getCreatedTime() {
        return createdTime;
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

    public void setPricePackageId(int pricePackageId) {
        this.pricePackageId = pricePackageId;
    }

    public int getPricePackageId() {
        return pricePackageId;
    }


}
