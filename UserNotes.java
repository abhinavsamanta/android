package com.example.firebasenoteapp;

import java.io.Serializable;

public class UserNotes implements Serializable {

    String notetitile="",notedesc="",notedate="",noteid="";

    public UserNotes(){

    }

    public UserNotes(String notetitile, String notedesc, String notedate, String noteid) {
        this.notetitile = notetitile;
        this.notedesc = notedesc;
        this.notedate = notedate;
        this.noteid = noteid;
    }

    public String getNotetitile() {
        return notetitile;
    }

    public void setNotetitile(String notetitile) {
        this.notetitile = notetitile;
    }

    public String getNotedesc() {
        return notedesc;
    }

    public void setNotedesc(String notedesc) {
        this.notedesc = notedesc;
    }

    public String getNotedate() {
        return notedate;
    }

    public void setNotedate(String notedate) {
        this.notedate = notedate;
    }

    public String getNoteid() {
        return noteid;
    }

    public void setNoteid(String noteid) {
        this.noteid = noteid;
    }
}
