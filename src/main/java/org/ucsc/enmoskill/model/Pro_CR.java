package org.ucsc.enmoskill.model;

import javax.servlet.http.HttpServletRequest;

public class Pro_CR {

    private String userid ,role, proposalid ,requestid;

    public Pro_CR(HttpServletRequest req){
        role = req.getParameter("Role");
        userid = req.getParameter("UserId");
        proposalid = req.getParameter("ProposalId");
        requestid = req.getParameter("RequestId");
    }

    public Boolean CheckReqiredFilds(){
        if (role == null){
            return false;
        }else {
            return true;
        }
    }

    public boolean isClient(){
        if (role.equals("Client")){
            return true;
        }else
            return false;
    }

    public boolean isDesigner(){
        if (role.equals("Designer")){
            return true;
        }else
            return false;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProposalid() {
        return proposalid;
    }

    public void setProposalid(String proposalid) {
        this.proposalid = proposalid;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }
}
