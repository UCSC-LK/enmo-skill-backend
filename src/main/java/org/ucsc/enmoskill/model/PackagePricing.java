package org.ucsc.enmoskill.model;

import java.util.HashMap;

public class PackagePricing {

    private int pricePackageId;
    private String type;
    private String deliveryDuration;
    private String noOfRevisions;

    private float price;

    private int packageId;

    private int noOfConcepts;

    private DeliverablesModel del;

    private HashMap<String, Integer> delMap;

    public PackagePricing(){
    }

//    public PackagePricing(int pricePackageId, String type, String deliveryDuration, String noOfRevisions, float price, int noOfConcepts, int packageId, PackageDeliverables deliverables){
//        this.pricePackageId = pricePackageId;
//        this.type = type;
//        this.deliveryDuration = deliveryDuration;
//        this.noOfRevisions = noOfRevisions;
//        this.price = price;
//        this.noOfConcepts = noOfConcepts;
//        this.packageId = packageId;
//        this.deliverables = deliverables;
//
//    }

    public PackagePricing(int pricePackageId, String type, String deliveryDuration, String noOfRevisions, float price, int noOfConcepts, int packageId, DeliverablesModel del){
        this.pricePackageId = pricePackageId;
        this.type = type;
        this.deliveryDuration = deliveryDuration;
        this.noOfRevisions = noOfRevisions;
        this.price = price;
        this.noOfConcepts = noOfConcepts;
        this.packageId = packageId;
        this.del = del;

    }


    public void setPricePackageId(int pricePackageId) {
        this.pricePackageId = pricePackageId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDeliveryDuration(String deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public void setNoOfRevisions(String noOfRevisions) {
        this.noOfRevisions = noOfRevisions;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setDel(DeliverablesModel del) {
        this.del = del;
    }

    public void setNoOfConcepts(int noOfConcepts) {
        this.noOfConcepts = noOfConcepts;
    }

    public void setDelMap(HashMap<String, Integer> delMap) {
        this.delMap = delMap;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getPricePackageId() {
        return pricePackageId;
    }

    public String getType() {
        return type;
    }

    public String getDeliveryDuration() {
        return deliveryDuration;
    }

    public String getNoOfRevisions() {
        return noOfRevisions;
    }

    public float getPrice() {
        return price;
    }

    public int getNoOfConcepts() {
        return noOfConcepts;
    }

    public int getPackageId() {
        return packageId;
    }

    public HashMap<String, Integer> getDelMap() {
        return delMap;
    }

    public DeliverablesModel getDel() {
        return del;
    }
}
