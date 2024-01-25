package org.ucsc.enmoskill.model;

import java.io.PrintWriter;

public class FlyerDesignDeliverables {
    private int pricePackageID;
    private int printReady;
    private int sourceFile;
    private int doubleSided;
    private int customGraphics;
    private int photoEditing;
    private int socialMediaDesign;
    private int commercialUse;

    public FlyerDesignDeliverables(){

    }

    public FlyerDesignDeliverables(int pricePackageID, int printReady, int sourceFile, int doubleSided, int customGraphics, int photoEditing, int socialMediaDesign, int commercialUse){
        this.pricePackageID = pricePackageID;
        this.printReady = printReady;
        this.sourceFile = sourceFile;
        this.doubleSided = doubleSided;
        this.customGraphics = customGraphics;
        this.photoEditing = photoEditing;
        this.socialMediaDesign = socialMediaDesign;
        this.commercialUse = commercialUse;
    }

    public int getCommercialUse() {
        return commercialUse;
    }

    public int getPricePackageID() {
        return pricePackageID;
    }

    public int getSourceFile() {
        return sourceFile;
    }

    public int getCustomGraphics() {
        return customGraphics;
    }

    public int getDoubleSided() {
        return doubleSided;
    }

    public int getPhotoEditing() {
        return photoEditing;
    }

    public int getPrintReady() {
        return printReady;
    }

    public int getSocialMediaDesign() {
        return socialMediaDesign;
    }

    public void setPricePackageID(int pricePackageID) {
        this.pricePackageID = pricePackageID;
    }

    public void setSourceFile(int sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setCommercialUse(int commercialUse) {
        this.commercialUse = commercialUse;
    }

    public void setCustomGraphics(int customGraphics) {
        this.customGraphics = customGraphics;
    }

    public void setDoubleSided(int doubleSided) {
        this.doubleSided = doubleSided;
    }

    public void setPhotoEditing(int photoEditing) {
        this.photoEditing = photoEditing;
    }

    public void setPrintReady(int printReady) {
        this.printReady = printReady;
    }

    public void setSocialMediaDesign(int socialMediaDesign) {
        this.socialMediaDesign = socialMediaDesign;
    }
}
