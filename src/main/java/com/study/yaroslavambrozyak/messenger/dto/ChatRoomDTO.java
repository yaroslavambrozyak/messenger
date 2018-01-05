package com.study.yaroslavambrozyak.messenger.dto;

import org.hibernate.validator.constraints.NotBlank;

public class ChatRoomDTO {

    @NotBlank
    private String name;
    @NotBlank
    private long creatorId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
