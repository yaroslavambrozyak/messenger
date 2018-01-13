package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserAlreadyExists;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, ChatRoomNotFoundException.class})
    public ResponseEntity<String> userNotFound(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<String> userAlreadyExist(UserAlreadyExists ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.CONFLICT);
    }
}
