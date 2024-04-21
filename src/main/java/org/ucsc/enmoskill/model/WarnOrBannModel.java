package org.ucsc.enmoskill.model;

public class WarnOrBannModel {

    private UserFullModel userFullModel;

    private Package apackage;

    public WarnOrBannModel() {
    }

    public WarnOrBannModel(UserFullModel userFullModel, Package apackage) {
        this.userFullModel = userFullModel;
        this.apackage = apackage;
    }

    public Package getApackage() {
        return apackage;
    }

    public UserFullModel getUserFullModel() {
        return userFullModel;
    }

    public void setApackage(Package apackage) {
        this.apackage = apackage;
    }

    public void setUserFullModel(UserFullModel userFullModel) {
        this.userFullModel = userFullModel;
    }
}
