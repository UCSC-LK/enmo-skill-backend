package org.ucsc.enmoskill.model;

public class DesignCategoryModel {
    private int categoryId;
    private int category;
    private int del_1;
    private int del_2;
    private int del_3;
    private int del_4;
    private int del_5;

    public DesignCategoryModel(){}

    public DesignCategoryModel(int categoryId, int category, int del_1, int del_2, int del_3, int del_4, int del_5){
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

    public int getDel_4() {
        return del_4;
    }

    public int getDel_3() {
        return del_3;
    }

    public int getDel_5() {
        return del_5;
    }

    public int getDel_2() {
        return del_2;
    }

    public int getCategory() {
        return category;
    }

    public int getDel_1() {
        return del_1;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setDel_5(int del_5) {
        this.del_5 = del_5;
    }

    public void setDel_4(int del_4) {
        this.del_4 = del_4;
    }

    public void setDel_3(int del_3) {
        this.del_3 = del_3;
    }

    public void setDel_2(int del_2) {
        this.del_2 = del_2;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setDel_1(int del_1) {
        this.del_1 = del_1;
    }


}
