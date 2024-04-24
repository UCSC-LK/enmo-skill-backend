package org.ucsc.enmoskill.model;

import java.util.HashMap;

public class AdminDashboardModel {

    private HashMap<String, Integer> categoryAnalytics;

    public AdminDashboardModel() {

    }

    public AdminDashboardModel(HashMap<String, Integer> categoryAnalytics) {
        this.categoryAnalytics = categoryAnalytics;
    }

    public HashMap<String, Integer> getCategoryAnalytics() {
        return categoryAnalytics;
    }


    public void setCategoryAnalytics(HashMap<String, Integer> categoryAnalytics) {
        this.categoryAnalytics = categoryAnalytics;
    }
}
