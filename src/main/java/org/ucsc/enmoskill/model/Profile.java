package org.ucsc.enmoskill.model;

import javax.servlet.http.HttpServletRequest;

public class Profile {
    private String userId,role;

    public Profile(HttpServletRequest req) {
        userId=req.getParameter("UserId");
        role=req.getParameter("Role");
    }

    public boolean CheckReqiredFields(){
        if(userId == null){
            return false;
        }else{
            return true;
        }
    }

    public Boolean isDesigner(){
        if(role.equals("Designer")){
            return true;
        }else return false;
    }

    public String getUserId() {
        return userId;
    }
    public String getrole() {
        return role;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setrole(String role) {
        this.userId = role;
    }
}
