package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.Message;

public interface ChatRoomService {

    ChatRoom getChatRoom(long id);
    void createChatRoom(ChatRoomDTO chatRoomDTO);
    void addUserToChat(long chatRoomId, long userId);
}
