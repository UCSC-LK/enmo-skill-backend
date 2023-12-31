package org.ucsc.enmoskill.model;

public class LogoDesignDeliverables {

    private int pricePackageId, logoTransparency, vectorFile, printableFile, mockup, sourceFile, socialMediaKit;

    public LogoDesignDeliverables(){

    }

    public LogoDesignDeliverables(int pricePackageId, int logoTransparency, int vectorFile, int printableFile, int mockup, int sourceFile, int socialMediaKit){
        this.pricePackageId = pricePackageId;
        this.logoTransparency = logoTransparency;
        this.vectorFile = vectorFile;
        this.printableFile = printableFile;
        this.mockup = mockup;
        this.sourceFile = sourceFile;
        this.socialMediaKit = socialMediaKit;


    }

    public int getPricePackageId() {
        return pricePackageId;
    }

    public int getLogoTransparency() {
        return logoTransparency;
    }

    public int getMockup() {
        return mockup;
    }

    public int getPrintableFile() {
        return printableFile;
    }

    public int getSocialMediaKit() {
        return socialMediaKit;
    }

    public int getSourceFile() {
        return sourceFile;
    }

    public int getVectorFile() {
        return vectorFile;
    }

    public void setPricePackageId(int pricePackageId) {
        this.pricePackageId = pricePackageId;
    }

    public void setLogoTransparency(int logoTransparency) {
        this.logoTransparency = logoTransparency;
    }

    public void setMockup(int mockup) {
        this.mockup = mockup;
    }

    public void setPrintableFile(int printableFile) {
        this.printableFile = printableFile;
    }

    public void setSocialMediaKit(int socialMediaKit) {
        this.socialMediaKit = socialMediaKit;
    }

    public void setSourceFile(int sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setVectorFile(int vectorFile) {
        this.vectorFile = vectorFile;
    }


}
