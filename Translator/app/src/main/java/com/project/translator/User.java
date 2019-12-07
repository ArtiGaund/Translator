package com.project.translator;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    String name;
    String email;
    String languageCode;
    String photoUrL;
    String uid;
    List<String> sent;
    List<String> recieved;

    public User(String name, String email, String languageCode, String photoUrL, String uid, List<String> sent, List<String> recieved) {
        this.name = name;
        this.email = email;
        this.languageCode = languageCode;
        this.photoUrL = photoUrL;
        this.uid = uid;
        this.sent = sent;
        this.recieved = recieved;
    }

    public String getUid() { return uid; }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public List<String> getSent() {
        return sent;
    }

    public List<String> getRecieved() {
        return recieved;
    }

    public String getPhotoUrL() {
        return photoUrL;
    }

    public void setPhotoUrL(String photoUrL) {
        this.photoUrL = photoUrL;
    }

    public User(User user)
    {
        this.name=user.getName();
        this.email=user.getEmail();
        this.photoUrL=user.getPhotoUrL();
        this.languageCode=user.getLanguageCode();
        this.uid = user.getUid();
        this.sent=user.getSent();
        this.recieved=user.getRecieved();
    }
    public User() {

    }

    public User(String name, String photoUrL) {
        this.name = name;
        this.photoUrL = photoUrL;
    }
}
