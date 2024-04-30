package org.ucsc.enmoskill.model;

import javax.servlet.http.HttpServletRequest;

public class Req_BRlist {
    private String userid,role;

    public Req_BRlist(HttpServletRequest req) {
        role = req.getParameter("Role");
        userid = req.getParameter("UserId");
    }

    public Boolean CheckReqiredFields(){
        if(role == null){
            return false;
        }else {
            return true;
        }
    }
    public boolean isClient(){
        if(role.equals("Client")){
            return true;
        }else return false;
    }
    public boolean isDesigner(){
        if(role.equals("Designer")){
            return true;
        }else return false;
    }

    public boolean isAgent(){
        if(role.equals("Support")){
            return true;
        }else return false;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
