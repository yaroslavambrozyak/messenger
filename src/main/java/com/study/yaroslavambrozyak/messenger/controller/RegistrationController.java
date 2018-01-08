package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/app")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> registration(@Validated RegistrationDTO registrationDTO){
        userService.createUser(registrationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}