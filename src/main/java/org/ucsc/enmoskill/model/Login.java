package org.ucsc.enmoskill.model;

public class Login {

    private String email;
    private String username;
    private String password;

    public Login(String email ,String username, String password) {
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
}
