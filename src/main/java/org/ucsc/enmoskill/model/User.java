package org.ucsc.enmoskill.model;

public class User {

    private String email;
    private String username;
    private String password;

    private  int id;
    private String user_role;

    private String name,contact_no,description,NIC,country,url;


        // Constructors (if needed)
    public User(String email , String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String email, String username, String password, int id, String user_role, String name, String contact_no, String description, String NIC, String country, String url) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.id = id;
        this.user_role = user_role;
        this.name = name;
        this.contact_no = contact_no;
        this.description = description;
        this.NIC = NIC;
        this.country = country;
        this.url = url;
    }


    public boolean checkRequired(){
        if(name!=null&&contact_no!=null&&description!=null&&NIC!=null&&country!=null){
            return true;
        }else return false;
    }

    public String getInsertUserDetails(){
        return String.format("UPDATE users SET name = '%s',contact_no = '%s',url = '%s' WHERE userid = %s;",this.name,this.contact_no,this.url,this.id);
    }
    public String getInsertClientDetails(){
        return String.format("INSERT INTO client (userid,  description, NIC, joinedDate, country) VALUES (%s, '%s', '%s',  CURDATE(), '%s');",this.id,this.description,this.NIC,this.country);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User() {

    }
}



