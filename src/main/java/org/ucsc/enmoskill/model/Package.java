package org.ucsc.enmoskill.model;

public class Package {
    private int packageId;
    private String title;
    private String description;
    private String category;
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

    public Package(int packageId ,String title, String description, String category, int designerUserId){
        this.packageId = packageId;
        this.title = title;
        this.description = description;
        this.category = category;
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

    public int getDesignerUserId() {
        return designerUserId;
    }


}
