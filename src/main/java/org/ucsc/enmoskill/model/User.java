package org.ucsc.enmoskill.model;

public class User {

    private String email;
    private String username;
    private String password;

    private  int id;
    private String user_role;


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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }
}



