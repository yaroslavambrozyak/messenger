package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/app")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable("id") Long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @GetMapping("/user/{id}/chats")
    public Set<ChatRoomDTO> getUserChats(@PathVariable("id") Long id) throws UserNotFoundException {
        return userService.getUserChats(id);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id, UserUpdateDTO user) throws UserNotFoundException {
        userService.updateUser(user,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}
