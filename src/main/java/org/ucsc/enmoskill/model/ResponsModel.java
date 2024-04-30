package org.ucsc.enmoskill.model;

public class ResponsModel {

    private String resMassage;
    private int resStatus;

    private int value;

    private boolean Status;

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

    public boolean IsSucess(){
        return Status;
    }

    public void setState(boolean status){
        this.Status = status;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
