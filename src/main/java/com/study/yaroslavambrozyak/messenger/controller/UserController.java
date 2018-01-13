package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/app")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public UserDTO getCurrUser(){
        return userService.getCurrentUser();
    }

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/user/{id}/chats")
    public Page<ChatRoomDTO> getUserChats(@PathVariable("id") long id, Pageable pageable) {
        return userService.getUserChats(id, pageable);
    }

    @PutMapping("/user/{id}")
    public void updateUser(@PathVariable("id") long id, UserUpdateDTO user) {
        userService.updateUser(user, id);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/user/{id}/add/{friendId}")
    public void addToFriends(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        userService.addFriend(id,friendId);
    }

    @DeleteMapping("/user/{id}/delete/{friendId}")
    public void deleteFromFriends(@PathVariable("id") long id, @PathVariable("friendId") long friendId){
        userService.deleteFriend(id,friendId);
    }

    @GetMapping("/user/{id}/friends")
    public Page<UserDTO> getUserFriends(@PathVariable("id") long id, Pageable pageable){
        return userService.getUserFriends(id, pageable);
    }

    @GetMapping("/user/{id}/friend.request")
    public Page<UserDTO> getUserFriendRequest(@PathVariable("id") long id, Pageable pageable){
        return userService.getUserFriendRequest(id,pageable);
    }

    @PostMapping("/user/{id}/friend.request/{friendId}")
    public void friendRequest(@PathVariable("id") long id, @PathVariable("friendId") long friendId){
        userService.friendRequest(id,friendId);
    }
}
