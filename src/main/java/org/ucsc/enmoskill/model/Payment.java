package org.ucsc.enmoskill.model;

import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private Timestamp paymentTime;
    private double amount;
    private int orderId;

    public Payment(){}

    public Payment(int paymentId, Timestamp paymentTime, double amount, int orderId){
        this.paymentId = paymentId;
        this.paymentTime = paymentTime;
        this.amount = amount;
        this.orderId = orderId;
    }

    public Payment(double amount, int orderId){
        this.paymentId = 0;
        this.paymentTime = null;
        this.amount = amount;
        this.orderId = orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public Timestamp getPaymentTime() {
        return paymentTime;
    }
}
