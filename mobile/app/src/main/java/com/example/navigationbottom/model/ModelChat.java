package com.example.navigationbottom.model;

public class ModelChat {
    private String message, timestamp;
    private Long receiver, sender;


    public ModelChat() {

    }

    public ModelChat(String message, String timestamp, Long receiver, Long sender) {
        this.message = message;
        this.timestamp = timestamp;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }
}
