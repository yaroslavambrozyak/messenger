package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomCreateDTO;
import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
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

    @GetMapping("/chat/{id}")
    public ChatRoomDTO getChatRoom(@PathVariable("id") long id)
            throws ChatRoomNotFoundException, UserNotFoundException {
        return chatRoomService.getChatRoom(id);
    }

    @GetMapping("/chat/{id}/messages")
    public Page<MessageDTO> getChatMessages(@PathVariable("id") long id, Pageable pageable)
            throws ChatRoomNotFoundException, UserNotFoundException {
        return chatRoomService.getChatMessages(id,pageable);
    }

    @GetMapping("/chat/{id}/users")
    public Page<UserDTO> getUsersInChat(@PathVariable("id") long id, Pageable pageable)
            throws ChatRoomNotFoundException, UserNotFoundException {
        return chatRoomService.getUsersInChat(id,pageable);
    }

    @PostMapping("/chat")
    @ResponseStatus(HttpStatus.CREATED)
    public long createChatRoom(@Validated ChatRoomCreateDTO chatRoomCreateDTO) throws UserNotFoundException {
        return chatRoomService.createChatRoom(chatRoomCreateDTO);
    }

    @PostMapping("/chat/{chatId}/add/{userId}")
    public void addUserToChat(@PathVariable("chatId") long chatId, @PathVariable("userId") long userId)
            throws UserNotFoundException, ChatRoomNotFoundException {
        chatRoomService.addUserToChat(chatId, userId);
    }
}
