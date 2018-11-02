package com.hackathon.headsupguys;

import java.io.Serializable;

public class Message implements Serializable {
    private int id;
    private String author;
    private String title;
    private String content;
    private String status;


    public Message(int id, String author, String title, String content, String status) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
