package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Skills {

    private String skils;

    public Skills(String skils) {
        this.skils = skils;
    }

    public Skills(ResultSet result) throws SQLException {
        this.skils = result.getString("skill");
    }

    public String getSkils() {
        return skils;
    }

    public void setSkils(String skils) {
        this.skils = skils;
    }
}
