package org.ucsc.enmoskill.model;

public class IllustrationDeliverables {
    private int pricePackageID;
    private int sourceFile;
    private int highResolution;
    private int background_scene;
    private  int colour;
    private int fullBody;
    private int commercialUse;


    public IllustrationDeliverables(){

    }
    public IllustrationDeliverables(int pricePackageID, int sourceFile, int highResolution, int background_scene, int colour, int fullBody, int commercialUse){
        this.pricePackageID = pricePackageID;
        this.sourceFile = sourceFile;
        this.highResolution = highResolution;
        this.background_scene = background_scene;
        this.colour = colour;
        this.fullBody = fullBody;
        this.commercialUse = commercialUse;
    }

    public int getSourceFile() {
        return sourceFile;
    }

    public int getBackground_Scene() {
        return background_scene;
    }

    public int getColour() {
        return colour;
    }

    public int getCommercialUse() {
        return commercialUse;
    }

    public int getFullBody() {
        return fullBody;
    }

    public int getHighResolution() {
        return highResolution;
    }

    public int getPricePackageID() {
        return pricePackageID;
    }

    public void setSourceFile(int sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setBackground_Scene(int background_Scene) {
        this.background_scene = background_Scene;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public void setCommercialUse(int commercialUse) {
        this.commercialUse = commercialUse;
    }

    public void setFullBody(int fullBody) {
        this.fullBody = fullBody;
    }

    public void setHighResolution(int highResolution) {
        this.highResolution = highResolution;
    }

    public void setPricePackageID(int pricePackageID) {
        this.pricePackageID = pricePackageID;
    }
}
