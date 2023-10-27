package org.ucsc.enmoskill.model;

public class User {

    private String email;
    private String username;
    private String password;


        // Constructors (if needed)
    public User(String email , String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    // Setter for name
    public void setEmail(String email) {
        this.username = email;
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



