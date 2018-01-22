package com.study.yaroslavambrozyak.messenger.dto;

public class ChatRoomDTO {

    private long id;
    private String name;

    public ChatRoomDTO() {
    }

    public ChatRoomDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
