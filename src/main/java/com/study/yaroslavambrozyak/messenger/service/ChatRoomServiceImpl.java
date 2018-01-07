package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.repository.ChatRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChatRoom getChatRoom(long id) {
        return chatRoomRepository.getOne(id);
    }

    @Override
    public void createChatRoom(ChatRoomDTO chatRoomDTO) {
        ChatRoom chatRoom = modelMapper.map(chatRoomDTO,ChatRoom.class);
        User user = userService.getUserEntity(chatRoomDTO.getCreatorId());
        chatRoom.getUsersInRoom().add(user);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void addUserToChat(long chatRoomId, long userId) {
        User user = userService.getUserEntity(userId);
        ChatRoom chatRoom = chatRoomRepository.getOne(chatRoomId);
        chatRoom.getUsersInRoom().add(user);
        chatRoomRepository.save(chatRoom);
    }

}
