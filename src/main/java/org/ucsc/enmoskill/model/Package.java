package org.ucsc.enmoskill.model;

public class Package {
    private int packageId;
    private String title;
    private String description;
    private String category;
    private String coverUrl;
    private int clicks;
    private int orders;
    private String cancellations;
    private String status;
    private int designerUserId;

    public Package(){
    }

    public Package(int packageId){
        this.packageId = packageId;
    }

    public Package(int packageId ,String title, String description, String category){
        this.packageId = packageId;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public Package(int packageId ,String title, String description, String category, String coverUrl, int clicks, int orders, String cancellations, String status, int designerUserId){
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
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public void setCancellations(String cancellations) {
        this.cancellations = cancellations;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDesignerUserId(int designerUserId) {
        this.designerUserId = designerUserId;
    }

    public int getPackageId() {
        return packageId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public int getClicks() {
        return clicks;
    }

    public int getOrders() {
        return orders;
    }

    public String getCancellations() {
        return cancellations;
    }

    public String getStatus() {
        return status;
    }

    public int getDesignerUserId() {
        return designerUserId;
    }


}
