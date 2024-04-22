package org.ucsc.enmoskill.model;

import java.sql.Date;

public class BannModel {

    private int bannId;
    private String reason;
    private Date date;
    private int userId;
    private int ticketId;

    public BannModel() {
    }

    public BannModel(int bannId, String reason, Date date, int userId, int ticketId) {
        this.bannId = bannId;
        this.reason = reason;
        this.date = date;
        this.userId = userId;
        this.ticketId = ticketId;
    }

    public int getBannId() {
        return bannId;
    }

    public void setBannId(int bannId) {
        this.bannId = bannId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }


}
