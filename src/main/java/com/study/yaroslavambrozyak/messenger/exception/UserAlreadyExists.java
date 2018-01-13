package com.study.yaroslavambrozyak.messenger.exception;

public class UserAlreadyExists extends RuntimeException {

    public UserAlreadyExists() {
    }

    public UserAlreadyExists(String message) {
        super(message);
    }
}
