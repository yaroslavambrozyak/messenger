package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.dto.MessageDateDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.repository.ChatRoomRepository;
import com.study.yaroslavambrozyak.messenger.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Set;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MessageDateDTO saveMessage(MessageDTO messageDTO, long chatId){
        ChatRoom chatRoom = chatRoomService.getChatRoomEntity(chatId);
        Message message = modelMapper.map(messageDTO, Message.class);
        message.setLocalDateTime(LocalDateTime.now());
        User user = userService.getUserEntity(messageDTO.getUserId());
        message.setUser(user);
        message.setChatRoom(chatRoom);
        return modelMapper.map(messageRepository.save(message),MessageDateDTO.class);
    }



}
