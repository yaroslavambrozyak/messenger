package com.study.yaroslavambrozyak.messenger.dto;

import org.hibernate.validator.constraints.NotBlank;

public class MessageDTO {

    @NotBlank
    private String text;
    @NotBlank
    private long userId;

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
