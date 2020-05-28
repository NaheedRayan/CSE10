package com.example.cse10;

public class BookArchive {

    private String username;
    private String id;
    private String book_name;
    private String book_url;
    private String timestamp;


    public BookArchive() {
        //constructor needed
    }

    public BookArchive(String username, String id, String book_name, String book_url, String timestamp) {
        this.username = username;
        this.id = id;
        this.book_name = book_name;
        this.book_url = book_url;
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

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_url() {
        return book_url;
    }

    public void setBook_url(String book_url) {
        this.book_url = book_url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

