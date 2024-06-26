package org.ucsc.enmoskill.model;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class ProfileModel {
    private String  role, fname, lname, display_name, description , joinedDate,NIC, url;
    private int userId;
    private List<String> skills;
    private List<String> language;

    // Constructors, getters, setters, and other methods remain unchanged

    // Modify the constructor to use List instead of Array
    public ProfileModel(int userId, String role, String fname, String lname, String display_name, String description, List<String> skills, List<String> language, String joinedDate) {
        this.userId = userId;
        this.role = role;
        this.fname = fname;
        this.lname = lname;
        this.display_name = display_name;
        this.description = description;
        this.skills = skills;
        this.language = language;
        this.joinedDate = joinedDate;

    }
    public ProfileModel(int userId, String role, String fname, String lname, String display_name, String description, List<String> skills, List<String> language,String NIC,String url) {
        this.userId = userId;
        this.role = role;
        this.fname = fname;
        this.lname = lname;
        this.display_name = display_name;
        this.description = description;
        this.skills = skills;
        this.language = language;
        this.NIC=NIC;
        this.url=NIC;
    }

    public ProfileModel(HttpServletRequest req) {
        role = req.getParameter("role");
        userId = Integer.parseInt(req.getParameter("userId"));
    }

    public ProfileModel(ResultSet result) throws SQLException {
        this.userId = result.getInt("userid");
        this.fname = result.getString("fname");
        this.lname = result.getString("lname");
        this.description = result.getString("description");
        this.display_name = result.getString("display_name");
        this.skills = Arrays.asList(result.getString("skills").split(","));
        this.language = Arrays.asList(result.getString("language").split(","));
        this.joinedDate = result.getString("joinedDate");
        this.url = result.getString("url");


    }

    public ProfileModel(ResultSet result,int one) throws SQLException {
        this.userId = result.getInt("userid");
        this.fname = result.getString("fname");
        this.lname = result.getString("lname");
        this.description = result.getString("description");
        this.display_name = result.getString("display_name");

        String skillsString = result.getString("skills");
        if (skillsString != null) {
            this.skills = Arrays.asList(skillsString.split(","));
        } else {
            this.skills = Collections.emptyList();
        }

        String languageString = result.getString("language");
        if (languageString != null) {
            this.language = Arrays.asList(languageString.split(","));
        } else {
            this.language = Collections.emptyList();
        }
        this.joinedDate = result.getString("joinedDate");
        this.url = result.getString("url");

    }

    public ProfileModel(ResultSet result,int one,int two) throws SQLException {
        this.userId = result.getInt("userid");
        this.fname = result.getString("fname");
        this.lname = result.getString("lname");
        this.description = result.getString("description");
        this.display_name = result.getString("display_name");

        String skillsString = result.getString("skills");
        if (skillsString != null) {
            this.skills = Arrays.asList(skillsString.split(","));
        } else {
            this.skills = Collections.emptyList();
        }

        String languageString = result.getString("language");
        if (languageString != null) {
            this.language = Arrays.asList(languageString.split(","));
        } else {
            this.language = Collections.emptyList();
        }
        this.joinedDate = result.getString("joinedDate");
        this.url = result.getString("url");

    }

    public String getQueryLevelUp(){
        String queryRoleLevelUp = "UPDATE enmo_database.user_level_mapping t " +
                                    "SET t.userlevelID = 2 " +
                                    "WHERE t.userID = " + this.userId ;

        return queryRoleLevelUp;

    }
//    public String getQuery1(){
//
//        String query1="INSERT INTO enmo_database.designer (userid,description, fname, lname,display_name,joinedDate,NIC) VALUES (\'"+userId+"\',\""+description+"\",\'"+ fname+"\', \'"+lname+"\',\'"+display_name+"\','2021.10.30',)";
//        return query1;
//    }

    public String getQuery2(){
        String query2="INSERT INTO enmo_database.skill_mapping(userId, skill_id) VALUES ";
        return query2;
    }

    public String getQuery3(){
        String query3="INSERT INTO enmo_database.language_mapping(userId, language_id) VALUES ";
        return query3;
    }


    public String getUpdateQuery1(){
        String updateQuery1 = "UPDATE enmo_database.designer SET description = \"" + description + "\", fname = \'" + fname + "\', lname = \'" + lname + "\', display_name = \'" + display_name + "\' WHERE userId = " +  userId;
        return updateQuery1;
    }

    public String deleteSkills(){
        String queryDeleteSkils = "DELETE FROM enmo_database.skill_mapping WHERE userId =" + getUserId();
        return queryDeleteSkils;
    }


    public String deleteLanguages(){
        String queryDeleteLanguage = "DELETE FROM enmo_database.language_mapping WHERE userId ="+ getUserId();

        return queryDeleteLanguage;
    }




    public boolean CheckReqiredFields(){
        if(userId == 0){
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


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
