package org.ucsc.enmoskill.model;

public class Login {

    private String email;
    private String username;
    private String password;
    private String id;
    private  String userLevelID;

    public Login(String userID,String email ,String username, String password, String userLevelID) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.id = userID;
        this.userLevelID = userLevelID;
    }

    public String getEmail() {
        return email;
    }

    // Setter for name
    public void setEmail(String email) {
        this.username = email;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserLevelID() {
        return userLevelID;
    }

    public void setUserLevelID(String userLevelID) {
        this.userLevelID = userLevelID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

