package com.example.cse10.ModelClass;


import com.google.firebase.firestore.Exclude;

public class Notice {
    private String username;
    private String id;
    private String textfield;
    private String imageurl;
    private String timestamp;

    //for storing the document name
    private String key ;

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public Notice() {
        //constructor needed
    }

    public Notice(String username, String id, String textfield, String imageurl, String timestamp) {
        this.username = username;
        this.id = id;
        this.textfield = textfield;
        this.imageurl = imageurl;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTextfield() {
        return textfield;
    }

    public void setTextfield(String textfield) {
        this.textfield = textfield;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

