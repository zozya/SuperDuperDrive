package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credential {

    private int credentialid;
    private String url;
    private String username;
    private String key;
    private String password;
    private int userid;

    private String decryptedPassword;

    public Credential(String url, String username, String key, String password, int userid) {
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
        this.userid = userid;
    }

    public Credential(int credentialid, String url, String username, String key, String password, int userid) {
        this.credentialid = credentialid;
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
        this.userid = userid;
    }

    public int getCredentialid() {
        return credentialid;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getKey() {
        return key;
    }

    public String getPassword() {
        return password;
    }

    public int getUserid() {
        return userid;
    }

    public void setCredentialid(int credentialid) {
        this.credentialid = credentialid;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public void setDecryptedPassword(String decryptedPassword) {
        this.decryptedPassword = decryptedPassword;
    }

}
