package org.ucsc.enmoskill.model;

public class BannerDesignDeliverables {
    private int pricePackageID, customGraphics, sourceFile, printReady;

    public BannerDesignDeliverables(){

    }

    public BannerDesignDeliverables(int pricePackageID, int customGraphics, int sourceFile, int printReady){
        this.pricePackageID = pricePackageID;
        this.customGraphics = customGraphics;
        this.sourceFile = sourceFile;
        this.printReady = printReady;
    }

    public void setCustomGraphics(int customGraphics) {
        this.customGraphics = customGraphics;
    }

    public void setPricePackageID(int pricePackageID) {
        this.pricePackageID = pricePackageID;
    }

    public void setSourceFile(int sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setPrintReady(int printReady) {
        this.printReady = printReady;
    }

    public int getCustomGraphics() {
        return customGraphics;
    }

    public int getPrintReady() {
        return printReady;
    }

    public int getPricePackageID() {
        return pricePackageID;
    }

    public int getSourceFile() {
        return sourceFile;
    }
}
