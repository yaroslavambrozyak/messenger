package com.study.yaroslavambrozyak.messenger.exception;

public class UserAlreadyExists extends Exception {

    public UserAlreadyExists() {
    }

    public UserAlreadyExists(String message) {
        super(message);
    }
}
