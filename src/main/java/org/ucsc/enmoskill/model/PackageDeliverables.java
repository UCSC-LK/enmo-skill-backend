package org.ucsc.enmoskill.model;

public class PackageDeliverables {
    private int deliverablesId, pricePackageId, deliverablesCount;
    private int transparentFile, vectorFile, printableFile, mockup, sourceFile, socialMediaKit, highResolution, background_scene, colour, fullBody, commercialUse;

    private int doubleSided, customGraphics, photoEditing;

    public PackageDeliverables(){

    }

    public PackageDeliverables(int deliverablesId, int pricePackageId, int deliverablesCount, int logoTransparency,
                               int vectorFile, int printableFile, int mockup, int sourceFile, int socialMediaKit, int highResolution,
                               int backgroundScene, int colour, int fullBody, int commercialUse, int doubleSided,
                               int customGraphics, int photoEditing) {
        this.deliverablesId = deliverablesId;
        this.pricePackageId = pricePackageId;
        this.deliverablesCount = deliverablesCount;
        this.transparentFile = logoTransparency;
        this.vectorFile = vectorFile;
        this.printableFile = printableFile;
        this.mockup = mockup;
        this.sourceFile = sourceFile;
        this.socialMediaKit = socialMediaKit;
        this.highResolution = highResolution;
        this.background_scene = backgroundScene;
        this.colour = colour;
        this.fullBody = fullBody;
        this.commercialUse = commercialUse;
        this.doubleSided = doubleSided;
        this.customGraphics = customGraphics;
        this.photoEditing = photoEditing;

    }

    public int getSourceFile() {
        return sourceFile;
    }

    public int getVectorFile() {
        return vectorFile;
    }

    public int getPrintableFile() {
        return printableFile;
    }

    public int getMockup() {
        return mockup;
    }

    public int getTransparentFile() {
        return transparentFile;
    }

    public int getPricePackageId() {
        return pricePackageId;
    }

    public int getSocialMediaKit() {
        return socialMediaKit;
    }

    public int getHighResolution() {
        return highResolution;
    }

    public int getDeliverablesCount() {
        return deliverablesCount;
    }

    public int getDeliverablesId() {
        return deliverablesId;
    }

    public int getCommercialUse() {
        return commercialUse;
    }

    public int getBackground_scene() {
        return background_scene;
    }

    public int getPhotoEditing() {
        return photoEditing;
    }

    public int getDoubleSided() {
        return doubleSided;
    }



    public int getCustomGraphics() {
        return customGraphics;
    }

    public int getColour() {
        return colour;
    }



    public int getFullBody() {
        return fullBody;
    }

    public void setSourceFile(int sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setHighResolution(int highResolution) {
        this.highResolution = highResolution;
    }

    public void setVectorFile(int vectorFile) {
        this.vectorFile = vectorFile;
    }

    public void setSocialMediaKit(int socialMediaKit) {
        this.socialMediaKit = socialMediaKit;
    }

    public void setPrintableFile(int printableFile) {
        this.printableFile = printableFile;
    }

    public void setFullBody(int fullBody) {
        this.fullBody = fullBody;
    }

    public void setMockup(int mockup) {
        this.mockup = mockup;
    }

    public void setCommercialUse(int commercialUse) {
        this.commercialUse = commercialUse;
    }

    public void setTransparentFile(int logoTransparency) {
        this.transparentFile = logoTransparency;
    }

    public void setPricePackageId(int pricePackageId) {
        this.pricePackageId = pricePackageId;
    }

    public void setDeliverablesCount(int deliverablesCount) {
        this.deliverablesCount = deliverablesCount;
    }

    public void setDeliverablesId(int deliverablesId) {
        this.deliverablesId = deliverablesId;
    }

    public void setPhotoEditing(int photoEditing) {
        this.photoEditing = photoEditing;
    }

    public void setBackground_scene(int background_scene) {
        this.background_scene = background_scene;
    }

    public void setDoubleSided(int doubleSided) {
        this.doubleSided = doubleSided;
    }



    public void setCustomGraphics(int customGraphics) {
        this.customGraphics = customGraphics;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }


}
