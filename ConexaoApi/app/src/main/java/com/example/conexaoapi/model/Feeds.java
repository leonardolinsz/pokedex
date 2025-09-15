package com.example.conexaoapi.model;

public class Feeds {
    private String userId;
    private String id;
    private String title;
    private String desc;

    public Feeds() {}

    public Feeds(String userId, String id, String title, String desc) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.desc = desc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
