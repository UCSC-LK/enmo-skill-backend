package org.ucsc.enmoskill.model;

import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewModel {

    private int package_id;
    private int review_id;
    private int stars;
    private String description;
    private Timestamp date;
    private int order_id;

    private int client_id;

    public ReviewModel(){}

    public ReviewModel( String description, int package_id, int review_id, int stars , int order_id , Timestamp date , int client_id){
        this.description= description;
        this.package_id = package_id;
        this.review_id = review_id;
        this.stars =stars;
        this.order_id =order_id;
        this.date= date;
        this.client_id = client_id;

    }

    public ReviewModel( ResultSet resultSet) throws SQLException{
        this.date =resultSet.getTimestamp("date");
        this.review_id = resultSet.getInt("review_id");
        this.order_id = resultSet.getInt("order_id");
        this.stars = resultSet.getInt("stars");
        this.package_id = resultSet.getInt("package_id");
        this.description = resultSet.getString("description");
        this.client_id = resultSet.getInt("client_id");
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }


    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getReview_id() {
        return review_id;
    }

    public int getPackage_id() {
        return package_id;
    }

    public int getStars() {
        return stars;
    }

    public Timestamp getDate() {
        return date;
    }
}