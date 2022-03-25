package com.joshuamccluskey.taskmaster.model;

public class Task {
    String title;
    String body;
    StateEnum state;

    public Task(String title, String body, StateEnum state) {
        this.title = title;
        this.body = body;
        this.state = state;
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

}