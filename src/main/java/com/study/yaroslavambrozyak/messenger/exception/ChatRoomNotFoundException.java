package com.study.yaroslavambrozyak.messenger.exception;

public class ChatRoomNotFoundException extends Exception{

    public ChatRoomNotFoundException() {
    }

    public ChatRoomNotFoundException(String message) {
        super(message);
    }
}
