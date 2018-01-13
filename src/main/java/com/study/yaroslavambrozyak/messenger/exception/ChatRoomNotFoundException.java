package com.study.yaroslavambrozyak.messenger.exception;

public class ChatRoomNotFoundException extends RuntimeException{

    public ChatRoomNotFoundException() {
    }

    public ChatRoomNotFoundException(String message) {
        super(message);
    }
}
