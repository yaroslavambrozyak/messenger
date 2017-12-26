package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;

public interface ChatRoomService {

    void createChatRoom(ChatRoomDTO chatRoomDTO);
    void addUserToChat(long chatRoomId, long userId);
}
