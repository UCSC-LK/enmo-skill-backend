package org.ucsc.enmoskill.model;

public class DeliverablesModel {

    private int deliverablesId;
    private int categoryId;
    private int pricePackageId;
    private int del_1;
    private int del_2;
    private int del_3;
    private int del_4;
    private int del_5;

    public DeliverablesModel(){

    }

    public DeliverablesModel(int deliverablesId, int categoryId, int pricePackageId, int del_1, int del_2, int del_3, int del_4, int del_5){
        this.deliverablesId = deliverablesId;
        this.categoryId = categoryId;
        this.pricePackageId = pricePackageId;
        this.del_1 = del_1;
        this.del_2 = del_2;
        this.del_3 = del_3;
        this.del_4 = del_4;
        this.del_5 = del_5;

    }

    public void setPricePackageId(int pricePackageId) {
        this.pricePackageId = pricePackageId;
    }

    public void setDeliverablesId(int deliverablesId) {
        this.deliverablesId = deliverablesId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setDel_1(int del_1) {
        this.del_1 = del_1;
    }

    public void setDel_2(int del_2) {
        this.del_2 = del_2;
    }

    public void setDel_3(int del_3) {
        this.del_3 = del_3;
    }

    public void setDel_4(int del_4) {
        this.del_4 = del_4;
    }

    public void setDel_5(int del_5) {
        this.del_5 = del_5;
    }

    public int getPricePackageId() {
        return pricePackageId;
    }

    public int getDeliverablesId() {
        return deliverablesId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getDel_1() {
        return del_1;
    }

    public int getDel_2() {
        return del_2;
    }

    public int getDel_3() {
        return del_3;
    }

    public int getDel_4() {
        return del_4;
    }

    public int getDel_5() {
        return del_5;
    }
}
