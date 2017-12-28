package com.study.yaroslavambrozyak.messenger.dto;

public class MessageDTO {

    private String text;
    private long userId;
    private long chatId;

    public MessageDTO() {
    }

    public MessageDTO(String text, long userId) {
        this.text = text;
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}
