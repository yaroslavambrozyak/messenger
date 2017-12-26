package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserService userService;

    @Override
    public void createChatRoom(ChatRoomDTO chatRoomDTO) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(chatRoomDTO.getName());
        User user = userService.getUserById(chatRoomDTO.getCreatorId());
        chatRoom.getUsersInRoom().add(user);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void addUserToChat(long chatRoomId, long userId) {
        User user = userService.getUserById(userId);
        ChatRoom chatRoom = chatRoomRepository.getOne(chatRoomId);
        chatRoom.getUsersInRoom().add(user);
        chatRoomRepository.save(chatRoom);
    }
}
