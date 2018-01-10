package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;

import java.util.Set;

public interface ChatRoomService {

    ChatRoomDTO getChatRoom(long id) throws ChatRoomNotFoundException, UserNotFoundException;
    long createChatRoom(ChatRoomDTO chatRoomDTO) throws UserNotFoundException;
    void addUserToChat(long chatRoomId, long userId) throws UserNotFoundException, ChatRoomNotFoundException;
    Set<MessageDTO> getChatMessages(long id) throws ChatRoomNotFoundException, UserNotFoundException;
    Set<UserDTO> getUsersInChat(long id) throws ChatRoomNotFoundException, UserNotFoundException;
    ChatRoom getChatRoomEntity(long id) throws ChatRoomNotFoundException, UserNotFoundException;
}
