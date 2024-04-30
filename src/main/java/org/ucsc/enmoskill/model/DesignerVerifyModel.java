package org.ucsc.enmoskill.model;

import java.sql.ResultSet;

public class DesignerVerifyModel {
    String Address,nicfront,nicback,designerID,designerName,desinerPhone;
    String email,url,NIC;

    public DesignerVerifyModel(ResultSet resultSet) {
        try {
            this.designerID = resultSet.getString("userid");
            this.designerName = resultSet.getString("fname") + " " + resultSet.getString("lname");
            this.desinerPhone = resultSet.getString("phone_no");
            this.email = resultSet.getString("email");
            this.Address = resultSet.getString("address");
            this.url = resultSet.getString("url");
            this.NIC = resultSet.getString("NIC");
            this.nicfront = resultSet.getString("nic_front");
            this.nicback = resultSet.getString("nic_back");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNicfront() {
        return nicfront;
    }

    public void setNicfront(String nicfront) {
        this.nicfront = nicfront;
    }

    public String getNicback() {
        return nicback;
    }

    public void setNicback(String nicback) {
        this.nicback = nicback;
    }

    public String getDesignerID() {
        return designerID;
    }

    public void setDesignerID(String designerID) {
        this.designerID = designerID;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getDesinerPhone() {
        return desinerPhone;
    }

    public void setDesinerPhone(String desinerPhone) {
        this.desinerPhone = desinerPhone;
    }
}
