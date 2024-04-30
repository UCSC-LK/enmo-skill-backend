package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProposalModel {

    private String proposalID ,deliveryDuration ,price,userID , requestID;
    private String title, description , packageId  ,pricingPackage;

    private String designerId;
    private String clientId;

    private String discription;

    private  int price_package_id;
    public ProposalModel(String proposalID,String packageId,String pricingPackage, String duration, String budget , String userID, String date, String description ,String requestID, String designerId, String clientId , String discription, int price_package_id){
        this.proposalID = proposalID;
        this.deliveryDuration = duration;
        this.price = budget;
        this.userID = userID;
        this.title = date;
        this.description =description;
        this.requestID = requestID;
        this.packageId = packageId;
        this.pricingPackage = pricingPackage;
        this.designerId = designerId;
        this.clientId = clientId;
        this.discription =discription;
        this.price_package_id = price_package_id;
    }

    public ProposalModel(ResultSet resultSet) throws SQLException {
        this.proposalID = resultSet.getString("proposalID");
        this.deliveryDuration = resultSet.getString("duration");
        this.price = resultSet.getString("price");
        this.userID = resultSet.getString("userID");
        this.title = resultSet.getString("title");
        this.description = resultSet.getString("description");
        this.requestID = resultSet.getString("requestID");
        this.pricingPackage = resultSet.getString("pricingPackage");
        this.packageId = resultSet.getString("packageId");
        this.designerId = resultSet.getString("designer_userID");
        this.clientId = resultSet.getString("client_userID");
        this.price_package_id = resultSet.getInt("price_package_id");
        this.discription = resultSet.getString("discription");
    }


    public int getPrice_package_id() {
        return price_package_id;
    }

    public String getDiscription() {
        return discription;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setPrice_package_id(int price_package_id) {
        this.price_package_id = price_package_id;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDesignerId() {
        return designerId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setDesignerId(String designerId) {
        this.designerId = designerId;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }


    public String getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(String deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPricingPackage() {
        return pricingPackage;
    }

    public void setPricingPackage(String pricingPackage) {
        this.pricingPackage = pricingPackage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getProposalID() {
        return proposalID;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

    public String getDuration() {
        return deliveryDuration;
    }

    public void setDuration(String duration) {
        this.deliveryDuration = duration;
    }

    public String getBudget() {
        return price;
    }

    public void setBudget(String budget) {
        this.price = budget;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDate() {
        return title;
    }

    public void setDate(String date) {
        this.title = date;
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
