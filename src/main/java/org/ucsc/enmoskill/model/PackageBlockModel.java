package org.ucsc.enmoskill.model;

import java.sql.Timestamp;
import java.util.List;

public class PackageBlockModel {
    private int packageId;
    private String title;
    private String description;
    private int category;
    private String coverUrl;
    private int clicks;
    private int orders;
    private String cancellations;
    private String status;
    private int designerUserId;
    private java.sql.Timestamp insertionTime;
    private int starterPrice;
    private int highestPrice;
    private int deliveryDuration;
    private float reviews;
    private String designerUserName;
    private String designerProfileImg;
    private List<Integer> languageId;

    public PackageBlockModel(){}

    public PackageBlockModel(int packageId, String title, String description, int category, String coverUrl, int clicks,
                             int orders, String cancellations, String status, int designerUserId, Timestamp insertionTime,
                             int starterPrice, int highestPrice, int deliveryDuration,
                             float reviews, String designerUserName, String designerProfileImg, List<Integer> languageId) {
        this.packageId = packageId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.coverUrl = coverUrl;
        this.clicks = clicks;
        this.orders = orders;
        this.cancellations = cancellations;
        this.status = status;
        this.designerUserId = designerUserId;
        this.insertionTime = insertionTime;
        this.starterPrice = starterPrice;
        this.highestPrice = highestPrice;
        this.deliveryDuration = deliveryDuration;
        this.reviews = reviews;
        this.designerUserName = designerUserName;
        this.designerProfileImg = designerProfileImg;
        this.languageId = languageId;
    }


    public String getTitle() {
        return title;
    }

    public Timestamp getInsertionTime() {
        return insertionTime;
    }

    public int getPackageId() {
        return packageId;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public String getStatus() {
        return status;
    }

    public String getCancellations() {
        return cancellations;
    }

    public int getOrders() {
        return orders;
    }

    public int getClicks() {
        return clicks;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public int getDesignerUserId() {
        return designerUserId;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    public int getHighestPrice() {
        return highestPrice;
    }

    public List<Integer> getLanguageId() {
        return languageId;
    }

    public float getReviews() {
        return reviews;
    }

    public int getStarterPrice() {
        return starterPrice;
    }

    public String getDesignerProfileImg() {
        return designerProfileImg;
    }

    public String getDesignerUserName() {
        return designerUserName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInsertionTime(Timestamp insertionTime) {
        this.insertionTime = insertionTime;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setCancellations(String cancellations) {
        this.cancellations = cancellations;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setDesignerUserId(int designerUserId) {
        this.designerUserId = designerUserId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDesignerProfileImg(String designerProfileImg) {
        this.designerProfileImg = designerProfileImg;
    }

    public void setDesignerUserName(String designerUserName) {
        this.designerUserName = designerUserName;
    }

    public void setHighestPrice(int highestPrice) {
        this.highestPrice = highestPrice;
    }

    public void setLanguageId(List<Integer> languageId) {
        this.languageId = languageId;
    }

    public void setReviews(float reviews) {
        this.reviews = reviews;
    }

    public void setStarterPrice(int starterPrice) {
        this.starterPrice = starterPrice;
    }


}
