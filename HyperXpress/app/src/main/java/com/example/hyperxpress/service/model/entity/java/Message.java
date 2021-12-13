package com.example.hyperxpress.service.model.entity.java;

public class Message {

    private String messagem;
    private Long timestamp;
    private String fromId;
    private String toId;

    public Message() {
    }

    public Message(String messagem, Long timestamp, String fromId, String toId) {
        this.messagem = messagem;
        this.timestamp = timestamp;
        this.fromId = fromId;
        this.toId = toId;
    }

    public String getMessagem() {
        return messagem;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }
}
