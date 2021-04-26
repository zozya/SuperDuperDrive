package com.udacity.jwdnd.course1.cloudstorage.model;

public class User {

    private int userid;
    private String username;
    private String salt;
    private String password;
    private String firstname;
    private String lastname;

    public User( String username, String salt, String password, String firstName, String lastName) {
        //this.userid = userid;
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.firstname = firstName;
        this.lastname = lastName;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public int getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getSalt() {
        return salt;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

}
