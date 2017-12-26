package com.study.yaroslavambrozyak.messenger.dto;

public class ChatRoomDTO {

    private String name;
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
