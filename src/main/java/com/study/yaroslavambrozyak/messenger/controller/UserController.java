package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.ErrorDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/app")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Get current user
     * @return current user data
     */
    @GetMapping("/user")
    public UserDTO getCurrUser(){
        return userService.getCurrentUser();
    }

    /**
     * Update current user
     * @param user updated user data
     */
    @PutMapping("/user")
    public void updateUser(UserUpdateDTO user) {
        userService.updateUser(user);
    }

    /**
     * Delete current user
     */
    @DeleteMapping("/user")
    public void deleteUser() {
        userService.deleteUser();
    }

    /**
     * Get all current user friends requests
     * @param pageable
     * @return list of friends requests
     */
    @GetMapping("/user/friend.request")
    public Page<UserDTO> getUserFriendRequest(Pageable pageable){
        return userService.getUserFriendRequest(pageable);
    }

    /**
     * Make friend request
     * @param friendId with whom you want to be friends
     */
    @PostMapping("/user/friend.request/{friendId}")
    public void friendRequest(@PathVariable("friendId") long friendId){
        userService.friendRequest(friendId);
    }

    /**
     * Get all current user friends
     * @param pageable
     * @return list of friends
     */
    @GetMapping("/user/friends")
    public Page<UserDTO> getUserFriends(Pageable pageable){
        return userService.getUserFriends(pageable);
    }

    /**
     * Accept user friend request and add to user to friend list
     * @param friendId
     */
    @PostMapping("/user/add/{friendId}")
    public void addToFriends(@PathVariable("friendId") long friendId) {
        userService.addFriend(friendId);
    }

    /**
     * Delete user from friend list
     * @param friendId whom you want to delete
     */
    @DeleteMapping("/user/delete/{friendId}")
    public void deleteFromFriends(@PathVariable("friendId") long friendId){
        userService.deleteFriend(friendId);
    }

    /**
     * Get all current user chats
     * @param pageable
     * @return list of chats
     */
    @GetMapping("/user/chats")
    public Page<ChatRoomDTO> getUserChats(Pageable pageable) {
        return userService.getUserChats(pageable);
    }

    /**
     * Get user by id
     * @param id
     * @return user data
     */
    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable("id") long id) {
        return userService.getUserById(id);
    }

    /**
     * Get user friend list
     * @param id
     * @param pageable
     * @return list of user friends
     */
    @GetMapping("/user/{id}/friends")
    public Page<UserDTO> getUserFriends(@PathVariable("id") long id, Pageable pageable){
        return userService.getUserFriends(id, pageable);
    }

    /**
     * Get user picture
     * @param id
     * @return user picture
     * @throws IOException
     */
    @GetMapping(value = "/user/{id}/picture", produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    public Resource loadPicture(@PathVariable("id") long id) throws IOException {
        return userService.loadPicture(id);
    }

    /**
     * Upload user picture
     * @param multipartFile
     * @throws IOException
     */
    @PostMapping("/user/picture")
    public void uploadPicture(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        userService.uploadPicture(multipartFile);
    }

    /**
     * Handle exception during picture uploading
     * @param e
     * @return error data
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO fileError(IOException e){
        logger.error("Upload error",e);
        return new ErrorDTO("Upload error");
    }
}
