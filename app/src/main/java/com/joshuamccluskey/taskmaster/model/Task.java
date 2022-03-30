package com.joshuamccluskey.taskmaster.model;


import java.util.Date;

public class Task {

    public long id;
    String title;
    String body;
    StateEnum state;
    java.util.Date date;



    public Task(String title, String body, StateEnum state, Date date) {
        this.title = title;
        this.body = body;
        this.state = state;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}