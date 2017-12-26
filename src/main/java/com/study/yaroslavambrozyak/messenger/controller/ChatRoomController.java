package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    @PostMapping("/chat")
    public ResponseEntity createChatRoom(ChatRoomDTO chatRoomDTO){
        chatRoomService.createChatRoom(chatRoomDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/chat/{chatId}/add/{userId}")
    public ResponseEntity addUserToChat(@PathVariable("chatId") long chatId, @PathVariable("userId") long userId){
        chatRoomService.addUserToChat(chatId,userId);
        return new ResponseEntity(HttpStatus.OK);
    }

}