package com.in28minutes.rest.webservices.restfulwebservices.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.in28minutes.rest.webservices.restfulwebservices.user.User;

public class Post {

    private Integer id;
    private String message;

    @JsonBackReference
    private User author;

    public Post() {}

    public Post(Integer id, String message, User author) {
        this.id = id;
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
