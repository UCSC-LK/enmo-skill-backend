package org.ucsc.enmoskill.model;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailsModel {

    private int order_details_id;
    private int orderID;
    private int designerId;
    private int clientId;

    private String client_message;
    private String designer_message;

    private String deliver_work;
    private Timestamp createdTime;


    public OrderDetailsModel(int order_details_id, int orderID, int designerId, int clientId, String client_message, String designer_message, String deliver_work, Timestamp createdTime){
        this.order_details_id = order_details_id;
        this.orderID =orderID;
        this.designerId = designerId;
        this.clientId = clientId;
        this.client_message=client_message;
        this.designer_message = designer_message;
        this.deliver_work =deliver_work;
        this.createdTime =createdTime;

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

    public OrderDetailsModel(ResultSet resultSet) throws SQLException{
        this.order_details_id = resultSet.getInt("order_details_id");
        this.orderID =resultSet.getInt("order_id");
        this.designerId = resultSet.getInt("designer_userID");
        this.clientId = resultSet.getInt("client_userID");
        this.client_message= resultSet.getString("client_message");
        this.designer_message = resultSet.getString("designer_message");
        this.deliver_work = resultSet.getString("deliver_work");
        this.createdTime =resultSet.getTimestamp("delivery_date");
    }

    public int getDesignerId() {
        return designerId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getOrder_details_id() {
        return order_details_id;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getClient_message() {
        return client_message;
    }

    public String getDeliver_work() {
        return deliver_work;
    }

    public String getDesigner_message() {
        return designer_message;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setDesignerId(int designerId) {
        this.designerId = designerId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setClient_message(String client_message) {
        this.client_message = client_message;
    }

    public void setOrder_details_id(int order_details_id) {
        this.order_details_id = order_details_id;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public void setDeliver_work(String deliver_work) {
        this.deliver_work = deliver_work;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setDesigner_message(String designer_message) {
        this.designer_message = designer_message;
    }

}

