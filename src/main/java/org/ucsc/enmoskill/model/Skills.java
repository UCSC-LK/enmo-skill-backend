package org.ucsc.enmoskill.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Skills {

    private String skills;
    private int skill_id;

    public Skills(String skils , int skill_id) {

        this.skills = skils;
        this.skill_id = skill_id;
    }

    public Skills(ResultSet result) throws SQLException {
        this.skills = result.getString("skill");
        this.skill_id = result.getInt("skill_id");
    }

    public String getSkils() {
        return skills;
    }

    public void setSkils(String skils) {
        this.skills = skils;
    }

    public int getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(int skill_id) {
        this.skill_id = skill_id;
    }
}
