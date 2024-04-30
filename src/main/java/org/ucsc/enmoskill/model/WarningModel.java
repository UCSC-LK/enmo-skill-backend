package org.ucsc.enmoskill.model;

import java.sql.Date;

public class WarningModel {
    private int warningId;
    private String reason;
    private Date date;
    private int userId;
    private int ticketId;

    public WarningModel() {
    }

    public WarningModel(int warningId, String reason, Date date, int userId, int ticketId) {
        this.warningId = warningId;
        this.reason = reason;
        this.date = date;
        this.userId = userId;
        this.ticketId = ticketId;
    }

    public int getWarningId() {
        return warningId;
    }

    public void setWarningId(int warningId) {
        this.warningId = warningId;
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
