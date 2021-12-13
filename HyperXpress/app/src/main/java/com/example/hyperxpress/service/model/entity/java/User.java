package com.example.hyperxpress.service.model.entity.java;

public class User {
    private String uuid;
    private String username;
    private String profileurl;

    public User() {
    }

    public User(String uuid, String username, String profileurl) {
        this.uuid = uuid;
        this.username = username;
        this.profileurl = profileurl;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileurl() {
        return profileurl;
    }
}
