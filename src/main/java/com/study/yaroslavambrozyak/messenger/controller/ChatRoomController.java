package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.service.ChatRoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/chat/{id}")
    public ChatRoomDTO getChatRoom(@PathVariable("id") long id) throws ChatRoomNotFoundException{
        return chatRoomService.getChatRoom(id);
    }

    @GetMapping("/chat/{id}/messages")
    public Set<MessageDTO> getChatMessages(@PathVariable("id") long id) throws ChatRoomNotFoundException {
        return chatRoomService.getChatMessages(id);
    }

    @GetMapping("/chat/{id}/users")
    public Set<UserDTO> getUsersInChat(@PathVariable("id") long id) throws ChatRoomNotFoundException {
        return chatRoomService.getUsersInChat(id);
    }

    @PostMapping("/chat")
    @ResponseStatus(HttpStatus.CREATED)
    public void createChatRoom(@Validated ChatRoomDTO chatRoomDTO) throws UserNotFoundException {
        chatRoomService.createChatRoom(chatRoomDTO);
    }

    @PostMapping("/chat/{chatId}/add/{userId}")
    public void addUserToChat(@PathVariable("chatId") long chatId, @PathVariable("userId") long userId) throws UserNotFoundException, ChatRoomNotFoundException {
        chatRoomService.addUserToChat(chatId,userId);
    }
}
