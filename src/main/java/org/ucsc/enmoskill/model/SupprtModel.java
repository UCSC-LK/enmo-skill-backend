package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupprtModel {
    private int requesterID,ref_no,status;
    private String description,subject,date;

    public SupprtModel(int requesterID, int ref_no, String description, String subject) {
        this.requesterID = requesterID;
        this.ref_no = ref_no;
        this.description = description;
        this.subject = subject;
    }
    public SupprtModel(ResultSet result) throws SQLException {
        this.requesterID = result.getInt("requesterID");
        this.ref_no = result.getInt("ref_no");
        this.description = result.getString("description");
        this.subject = result.getString("subject");
        this.date = result.getString("date");
        this.status = result.getInt("status");

    }

    public SupprtModel(ResultSet result, String popup) throws SQLException {
        this.requesterID = result.getInt("requesterID");
        this.ref_no = result.getInt("ticketID");
        this.description = result.getString("description");
        this.subject = result.getString("subject");
        this.date = result.getString("date");
    }


    public String getQuery(){
        Date Today= new Date();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);
        String query="INSERT INTO enmo_database.ticket (description, date, requesterID, subject,status) VALUES (\'"+description+"\', \'"+Date+"\',"+ requesterID+", \'"+subject+"\',1)";
        return query;

    }

    public String setHistoryData(){
        String query = "INSERT INTO enmo_database.ticket_history (ticketID, description, date, requesterID, subject)\n" +
                "SELECT ref_no, description, date, requesterID, subject\n" +
                "FROM ticket\n" +
                "WHERE ref_no = \'" + ref_no+"\' AND status = 1" ;
        return query;
    }

    public String getUpdatedQuery(){

        Date Today= new Date();
        String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);

        String query ="UPDATE enmo_database.ticket t SET t.description = \'" + description + "\', t.subject = \'" + subject + "\', t.date = \'" + Date + "\'\n" +
                        "WHERE t.ref_no="+ref_no;

        return query;
    }

    public int getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(int requesterID) {
        this.requesterID = requesterID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public int getRef_no() {
        return ref_no;
    }

    public void setRef_no(int ref_no) {
        this.ref_no = ref_no;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
