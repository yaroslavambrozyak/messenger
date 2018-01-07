package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.entity.Message;
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

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/chat/{id}/messages")
    public Set<MessageDTO> getChatMessages(@PathVariable("id") long id){
        return chatRoomService.getChatRoom(id)
                .getMessages()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toSet());
    }

    @GetMapping("/chat/{id}/users")
    public Set<UserDTO> getUsersInChat(@PathVariable("id") long id){
        return chatRoomService.getChatRoom(id)
                .getUsersInRoom()
                .stream()
                .map(user -> modelMapper.map(user,UserDTO.class))
                .collect(Collectors.toSet());

    }

    @PostMapping("/chat")
    public ResponseEntity<Void> createChatRoom(@Validated ChatRoomDTO chatRoomDTO){
        chatRoomService.createChatRoom(chatRoomDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/chat/{chatId}/add/{userId}")
    public ResponseEntity<Void> addUserToChat(@PathVariable("chatId") long chatId, @PathVariable("userId") long userId){
        chatRoomService.addUserToChat(chatId,userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private MessageDTO convertToDTO(Message message){
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setText(message.getText());
        messageDTO.setUserId(message.getUser().getId());
        return messageDTO;
    }

}
