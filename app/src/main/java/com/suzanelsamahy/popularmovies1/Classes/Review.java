package com.suzanelsamahy.popularmovies1.Classes;

public class Review {


    private String id;
    private String author;
    private String content;

    public Review() {

    }


    public String getRevID() {
        return id;
    }

    public void setRevID(String id) {
       this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

}


