package org.ucsc.enmoskill.model;

public class DesignerDashboardModel {
    public int pendingOrders;
    public int completedOrders;
    public double totalEarnings;
    public double userRatings;
    ProfileModel profileModel;
    public int designerId;

    public DesignerDashboardModel(){

    }

    public DesignerDashboardModel(int pendingOrders, int completedOrders, Double totalEarnings, double userRatings, ProfileModel profileModel, int designerId){
        this.pendingOrders=pendingOrders;
        this.profileModel = profileModel;
        this.completedOrders = completedOrders;
        this.userRatings = userRatings;
        this.totalEarnings = totalEarnings;
        this.designerId = designerId;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public double getUserRatings() {
        return userRatings;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public int getDesignerId() {
        return designerId;
    }

    public void setProfileModel(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }

    public void setPendingOrders(int pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public void setUserRatings(double userRatings) {
        this.userRatings = userRatings;
    }

    public void setDesignerId(int designerId) {
        this.designerId = designerId;
    }
}
