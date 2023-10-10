package org.ucsc.enmoskill.model;

public class Req_BRlist {
    private String userid,role;

    public Req_BRlist(String userid, String role) {
        this.userid = userid;
        this.role = role;
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
