package com.study.yaroslavambrozyak.messenger.dto;

import org.hibernate.validator.constraints.NotBlank;

public class MessageDTO {

    @NotBlank
    private String text;
    private long userId;

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

}
