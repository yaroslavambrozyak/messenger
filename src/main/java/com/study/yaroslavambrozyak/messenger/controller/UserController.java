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

import java.util.Locale;
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

    @GetMapping("/user/chats")
    public Page<ChatRoomDTO> getUserChats(Pageable pageable) {
        return userService.getUserChats(pageable);
    }

    @PutMapping("/user")
    public void updateUser(UserUpdateDTO user) {
        userService.updateUser(user);
    }

    @DeleteMapping("/user")
    public void deleteUser() {
        userService.deleteUser();
    }

    @GetMapping("/user/friend.request")
    public Page<UserDTO> getUserFriendRequest(Pageable pageable){
        return userService.getUserFriendRequest(pageable);
    }

    @PostMapping("/user/friend.request/{friendId}")
    public void friendRequest(@PathVariable("friendId") long friendId){
        userService.friendRequest(friendId);
    }

    @PostMapping("/user/add/{friendId}")
    public void addToFriends(@PathVariable("friendId") long friendId) {
        userService.addFriend(friendId);
    }

    @DeleteMapping("/user/delete/{friendId}")
    public void deleteFromFriends(@PathVariable("friendId") long friendId){
        userService.deleteFriend(friendId);
    }

    @GetMapping("/user/friends")
    public Page<UserDTO> getUserFriends(Pageable pageable){
        return userService.getUserFriends(pageable);
    }

    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/user/{id}/friends")
    public Page<UserDTO> getUserFriends(@PathVariable("id") long id, Pageable pageable){
        return userService.getUserFriends(id, pageable);
    }
}
