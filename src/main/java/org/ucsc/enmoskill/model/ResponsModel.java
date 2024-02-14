package org.ucsc.enmoskill.model;

public class ResponsModel {

    private String resMassage;
    private int resStatus;

    public ResponsModel(String resMassage, int resStatus) {
        this.resMassage = resMassage;
        this.resStatus = resStatus;
    }

    public String getResMassage() {
        return resMassage;
    }

    public int getResStatus() {
        return resStatus;
    }
}
