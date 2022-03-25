package com.joshuamccluskey.taskmaster.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    String title;
    String body;
    StateEnum state;
    Date date;



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