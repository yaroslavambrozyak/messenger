package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.*;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.service.ChatRoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    /**
     * Get chat room by id
     * @param id
     * @return chat room data
     */
    @GetMapping("/chat/{id}")
    public ChatRoomDTO getChatRoom(@PathVariable("id") long id) {
        return chatRoomService.getChatRoom(id);
    }

    /**
     * Create new chat room
     * @param chatRoomCreateDTO chat room data
     * @return chat`s id
     */
    @PostMapping("/chat")
    @ResponseStatus(HttpStatus.CREATED)
    public long createChatRoom(@Validated ChatRoomCreateDTO chatRoomCreateDTO) {
        return chatRoomService.createChatRoom(chatRoomCreateDTO);
    }

    /**
     * Update chat room by id
     * @param id
     * @param chatRoomCreateDTO chat room data
     */
    @PutMapping("/chat/{id}")
    public void updateChatRoom(@PathVariable("id") long id, @Validated ChatRoomCreateDTO chatRoomCreateDTO) {
        chatRoomService.updateChatRoom(id, chatRoomCreateDTO);
    }

    /**
     * Delete chat room by id
     * @param id
     */
    @DeleteMapping("/chat/{id}")
    public void deleteChatRoom(@PathVariable("id") long id){
        chatRoomService.deleteChatRoom(id);
    }

    /**
     * Get all users in chat by id
     * @param id
     * @param pageable
     * @return list of users
     */
    @GetMapping("/chat/{id}/users")
    public Page<UserDTO> getUsersInChat(@PathVariable("id") long id, Pageable pageable) {
        return chatRoomService.getUsersInChat(id, pageable);
    }

    /**
     * Add new user in chat
     * @param chatId in which chat you add user
     * @param userId which user you add to chat
     */
    @PostMapping("/chat/{chatId}/add/{userId}")
    public void addUserToChat(@PathVariable("chatId") long chatId, @PathVariable("userId") long userId) {
        chatRoomService.addUserToChat(chatId, userId);
    }

    /**
     * Delete user from chat
     * @param chatId from which chat you delete user
     * @param userId which user you delete
     */
    @DeleteMapping("/chat/{chatId}/delete/{userId}")
    public void deleteUserFromChat(@PathVariable("chatId") long chatId, @PathVariable("userId") long userId) {
        chatRoomService.deleteUserFromChat(chatId, userId);
    }

    /**
     * Get all messages from chat by id
     * @param id
     * @param pageable
     * @return list of messages
     */
    @GetMapping("/chat/{id}/messages")
    public Page<MessageDateDTO> getChatMessages(@PathVariable("id") long id, Pageable pageable) {
        return chatRoomService.getChatMessages(id, pageable);
    }
}
