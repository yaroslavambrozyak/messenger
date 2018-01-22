package com.study.yaroslavambrozyak.messenger.dto;

import org.hibernate.validator.constraints.NotBlank;

public class ChatRoomCreateDTO {

    @NotBlank
    private String name;

    public ChatRoomCreateDTO() {
    }

    public ChatRoomCreateDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
