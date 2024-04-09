package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupprtModel {
    private int requesterID,ref_no,status,agentID,order,packages,urgent,roleID;

    private String description,subject,date,role,email,userName,url,fileURL,admin;

    public SupprtModel(int requesterID, int ref_no, String description, String subject,String role,int agentID,int order,int packages,int urgent, String fileURL,String admin) {
        this.requesterID = requesterID;
        this.ref_no = ref_no;
        this.description = description;
        this.subject = subject;
        this.role = role;
        this.agentID=agentID;
        this.order=order;
        this.packages=packages;
        this.urgent=urgent;
        this.fileURL=fileURL;
        this.admin=admin;

    }
    public SupprtModel(ResultSet result) throws SQLException {
        this.requesterID = result.getInt("requesterID");
        this.ref_no = result.getInt("ref_no");
        this.description = result.getString("description");
        this.subject = result.getString("subject");
        this.date = result.getString("date");
        this.status = result.getInt("status");
        this.order = result.getInt("order");
        this.packages = result.getInt("packages");
        this.urgent=result.getInt("urgent");
        this.fileURL = result.getString("fileURL");
        this.admin = result.getString("assign_ad");

    }

    public SupprtModel(ResultSet result, String popup) throws SQLException {
        this.requesterID = result.getInt("requesterID");
        this.ref_no = result.getInt("ticketID");
        this.description = result.getString("description");
//        this.subject = result.getString("subject");
        this.date = result.getString("date");
        this.roleID=result.getInt("userlevelID");
    }

    public SupprtModel(ResultSet result, String comment,boolean agent) throws SQLException {
        this.agentID = result.getInt("agent_id");
        this.date = result.getString("date");
        this.description = result.getString("comment");
    }

//    public SupprtModel(ResultSet result,boolean agent,boolean adminComment) throws SQLException {
//        this.ref_no = result.getInt("ticket_id");
//        this.agentID = result.getInt("agent_id");
//        this.agent_description = result.getString("agent_description");
//        this.description = result.getString("comment");
//        this.date = result.getString("date");
//    }

    public SupprtModel(ResultSet result, String TicketID,int a) throws SQLException {
        this.description = result.getString("description");
        this.subject = result.getString("subject");
        this.date = result.getString("date");
        this.status = result.getInt("status");
        this.order = result.getInt("order");
        this.packages = result.getInt("packages");
        this.urgent=result.getInt("urgent");
        this.fileURL = result.getString("fileURL");
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
        this.order = result.getInt("order");
        this.packages = result.getInt("packages");
        this.urgent=result.getInt("urgent");
        this.fileURL = result.getString("fileURL");
        this.admin = result.getString("assign_ad");
    }


    public String getQuery(){
//        Date Today= new Date();
//        String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);
//        String query="INSERT INTO enmo_database.ticket (description, date, requesterID, subject,status) VALUES (\""+description+"\", \'"+Date+"\',"+ requesterID+", \""+subject+"\",1)";

        String query="INSERT INTO enmo_database.ticket (description, date, requesterID, subject,fileURL, `order`,`packages`, `status`) VALUES (?, ?, ?, ?, COALESCE(?, ''), ?,?, 1)";


        return query;

    }

    public String setHistoryData(){

//        String query = "INSERT INTO enmo_database.ticket_history th (ticketID, description, date, requesterID) " +
//                "LEFT JOIN ticket t ON (t.ref_no = th.ticketID) "+
//                "VALUES (t.ref_no, ?, ?, ?) "+
//                "WHERE ((t.status = 2 OR t.status = 1) AND t.ref_no = " + ref_no;

//        String query ="INSERT INTO enmo_database.ticket_history (ticketID, description, date, requesterID)\n" +
//                "SELECT t.ref_no, ?, ?, ?\n" +
//                "FROM ticket t\n" +
//                "WHERE ";
        String query = "INSERT INTO enmo_database.ticket_history (ticketID, description, date, requesterID) " +
                "SELECT t.ref_no, ?, ?, ? "+
                "FROM ticket t "+
                "WHERE (t.status = 2 OR t.status = 1) AND t.ref_no = " + ref_no;


        return query;
    }

//    public String getUpdatedQuery(){
//
////        Date Today= new Date();
////        String Date = new SimpleDateFormat("yyyy-MM-dd").format(Today);
//
////        String query ="UPDATE enmo_database.ticket t SET t.description = \"" + description + "\", t.subject = \"" + subject + "\", t.date = \'" + Date + "\'\n" +
////                        "WHERE t.ref_no="+ref_no;
//
//        String query ="UPDATE enmo_database.ticket SET description = ?, subject = ?, date =? "+
//                       "WHERE ref_no="+ref_no;
//
//        return query;
//    }


    public int getUrgent() {
        return urgent;
    }

    public void setUrgent(int urgent) {
        this.urgent = urgent;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getPackages() {
        return packages;
    }

    public void setPackages(int packages) {
        this.packages = packages;
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
