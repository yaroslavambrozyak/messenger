package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.ErrorDTO;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.SameUserException;
import com.study.yaroslavambrozyak.messenger.exception.UserAlreadyExists;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler({UserNotFoundException.class, ChatRoomNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO userNotFound(Exception ex){
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler({UserAlreadyExists.class, SameUserException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO userAlreadyExist(Exception ex){
        return new ErrorDTO(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO serverError(Exception ex){
        logger.error("ERROR",ex);
        return new ErrorDTO("Something went wrong");
    }
}
