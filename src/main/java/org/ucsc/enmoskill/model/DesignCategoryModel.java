package org.ucsc.enmoskill.model;

public class DesignCategoryModel {
    private int categoryId;
    private String category;
    private String del_1;
    private String del_2;
    private String del_3;
    private String del_4;
    private String del_5;

    public DesignCategoryModel(){}

    public DesignCategoryModel(int categoryId, String category, String del_1, String del_2, String del_3, String del_4, String del_5){
        this.categoryId = categoryId;
        this.category = category;
        this.del_1 = del_1;
        this.del_2 = del_2;
        this.del_3 = del_3;
        this.del_4 = del_4;
        this.del_5 = del_5;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDel_4() {
        return del_4;
    }

    public String getDel_3() {
        return del_3;
    }

    public String getDel_5() {
        return del_5;
    }

    public String getDel_2() {
        return del_2;
    }

    public String getCategory() {
        return category;
    }

    public String getDel_1() {
        return del_1;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setDel_5(String del_5) {
        this.del_5 = del_5;
    }

    public void setDel_4(String del_4) {
        this.del_4 = del_4;
    }

    public void setDel_3(String del_3) {
        this.del_3 = del_3;
    }

    public void setDel_2(String del_2) {
        this.del_2 = del_2;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDel_1(String del_1) {
        this.del_1 = del_1;
    }


}
