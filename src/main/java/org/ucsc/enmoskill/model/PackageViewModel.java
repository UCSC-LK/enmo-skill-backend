package org.ucsc.enmoskill.model;

import java.util.List;

public class PackageViewModel {
    private Package packageModel;
    private List<PackagePricing> pricings;
    private ProfileModel profileModel;
    private UserFullModel userFullModel;

    private int pendingOrders;
    private double userRatings;
    private double packageRatings;

    /// REVIEW DATA GOES HERE

    public PackageViewModel(){}

    public PackageViewModel(Package packageModel, List<PackagePricing> pricings, ProfileModel profileModel){
        this.packageModel = packageModel;
        this.pricings = pricings;
        this.profileModel = profileModel;
    }

    public void setPackageModel(Package packageModel) {
        this.packageModel = packageModel;
    }

    public void setPricings(List<PackagePricing> pricings) {
        this.pricings = pricings;
    }

    public void setProfileModel(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    public Package getPackageModel() {
        return packageModel;
    }

    public List<PackagePricing> getPricings() {
        return pricings;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    public UserFullModel getUserFullModel() {
        return userFullModel;
    }

    public void setUserFullModel(UserFullModel userFullModel) {
        this.userFullModel = userFullModel;
    }

    public int getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(int pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public double getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(double userRatings) {
        this.userRatings = userRatings;
    }

    public double getPackageRatings() {
        return packageRatings;
    }

    public void setPackageRatings(double packageRatings) {
        this.packageRatings = packageRatings;
    }


}

