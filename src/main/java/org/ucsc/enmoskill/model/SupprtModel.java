package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupprtModel {
    private int requesterID,ref_no,status,agentID;

    private String description,subject,date,role,email,userName,url;

    public SupprtModel(int requesterID, int ref_no, String description, String subject,String role,int agentID) {
        this.requesterID = requesterID;
        this.ref_no = ref_no;
        this.description = description;
        this.subject = subject;
        this.role = role;
        this.agentID=agentID;
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

    public SupprtModel(ResultSet result, String TicketID,int a) throws SQLException {
        this.description = result.getString("description");
        this.subject = result.getString("subject");
        this.date = result.getString("date");
        this.status = result.getInt("status");
    }

    public SupprtModel(ResultSet result,boolean agent) throws SQLException {
        this.requesterID = result.getInt("requesterID");
        this.ref_no = result.getInt("ref_no");
        this.userName = result.getString("username");
        this.email = result.getString("email");
        this.description = result.getString("description");
        this.subject = result.getString("subject");
        this.date = result.getString("date");
        this.status = result.getInt("status");
        this.role = result.getString("userlevelID");;
        this.agentID=result.getInt("agentID");
        this.url=result.getString("url");

    }


    public String getQuery(){
//        Date Today= new Date();
//        String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);
//        String query="INSERT INTO enmo_database.ticket (description, date, requesterID, subject,status) VALUES (\""+description+"\", \'"+Date+"\',"+ requesterID+", \""+subject+"\",1)";

        String query="INSERT INTO enmo_database.ticket (description, date, requesterID, subject,status) VALUES ( ?,?,?,?,1)";

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

//        Date Today= new Date();
//        String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);

//        String query ="UPDATE enmo_database.ticket t SET t.description = \"" + description + "\", t.subject = \"" + subject + "\", t.date = \'" + Date + "\'\n" +
//                        "WHERE t.ref_no="+ref_no;

        String query ="UPDATE enmo_database.ticket SET description = ?, subject = ?, date =? "+
                       "WHERE ref_no="+ref_no;

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
