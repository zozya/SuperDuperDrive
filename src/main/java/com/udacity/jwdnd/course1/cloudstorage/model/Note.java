package com.udacity.jwdnd.course1.cloudstorage.model;

public class Note {

    private int noteid;
    private String notetitle;
    private String notedescription;
    private int userid;

    public Note(String notetitle, String notedescription, int userid) {
        this.notetitle = notetitle;
        this.notedescription = notedescription;
        this.userid = userid;
    }

    public Note(int noteid, String notetitle, String notedescription, int userid) {
        this.noteid = noteid;
        this.notetitle = notetitle;
        this.notedescription = notedescription;
        this.userid = userid;
    }

    public int getNoteid() {
        return noteid;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public int getUserid() {
        return userid;
    }

    public void setNoteid(int noteid) {
        this.noteid = noteid;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
