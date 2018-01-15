package com.study.yaroslavambrozyak.messenger.exception;

public class SameUserException extends RuntimeException {

    public SameUserException() {
    }

    public SameUserException(String message) {
        super(message);
    }
}
