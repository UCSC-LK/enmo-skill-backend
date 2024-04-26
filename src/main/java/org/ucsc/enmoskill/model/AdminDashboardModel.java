package org.ucsc.enmoskill.model;

import java.sql.Date;
import java.util.HashMap;

public class AdminDashboardModel {

    private HashMap<String, Integer> categoryAnalytics;
    private HashMap<Date, Double> date_orders;

    private int userCount;
    private int packageCount;
    private double totalEarnings;

    private int activeOrders;
    private int completedOrders;



    public AdminDashboardModel() {

    }

    public AdminDashboardModel(HashMap<String, Integer> categoryAnalytics, int userCount, int packageCount, int activeOrders, int completedOrders, double totalEarnings) {
        this.categoryAnalytics = categoryAnalytics;
        this.userCount = userCount;
        this.packageCount = packageCount;
        this.activeOrders = activeOrders;
        this.completedOrders = completedOrders;
        this.totalEarnings = totalEarnings;
    }

    public HashMap<String, Integer> getCategoryAnalytics() {
        return categoryAnalytics;
    }


    public void setCategoryAnalytics(HashMap<String, Integer> categoryAnalytics) {
        this.categoryAnalytics = categoryAnalytics;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public int getActiveOrders() {
        return activeOrders;
    }

    public void setActiveOrders(int activeOrders) {
        this.activeOrders = activeOrders;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public HashMap<Date, Double> getDate_orders() {
        return date_orders;
    }

    public void setDate_orders(HashMap<Date, Double> date_orders) {
        this.date_orders = date_orders;
    }
}
