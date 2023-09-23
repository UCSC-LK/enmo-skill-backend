package org.ucsc.enmoskill.model;

public class User {

    private String username;
    private String password;

        // Constructors (if needed)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter for name
    public String getUsername() {
        return username;
    }

    // Setter for name
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for email
    public String getPassword() {
        return password;
    }

    // Setter for email
    public void setPassword(String password) {
        this.password = password;
    }
    }



