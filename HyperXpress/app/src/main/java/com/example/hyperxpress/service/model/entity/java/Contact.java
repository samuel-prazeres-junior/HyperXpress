package com.example.hyperxpress.service.model.entity.java;

public class Contact {
    String uuid;
    String username;
    String latestMessage;
    Long timestamp;
    String photoUrl;

    public Contact() {
    }

    public Contact(String uuid, String username, String latestMessage, Long timestamp, String photoUrl) {
        this.uuid = uuid;
        this.username = username;
        this.latestMessage = latestMessage;
        this.timestamp = timestamp;
        this.photoUrl = photoUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
