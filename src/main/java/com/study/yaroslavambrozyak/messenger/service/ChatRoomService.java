package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.*;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ChatRoomService {

    ChatRoomDTO getChatRoom(long id);

    long createChatRoom(ChatRoomCreateDTO chatRoomCreateDTO);

    void addUserToChat(long chatRoomId, long userId);

    Page<MessageDateDTO> getChatMessages(long id, Pageable pageable);

    Page<UserDTO> getUsersInChat(long id, Pageable pageable);

    ChatRoom getChatRoomEntity(long id);

    void deleteUserFromChat(long chatRoomId, long userId);

    void updateChatRoom(long chatRoomId, ChatRoomCreateDTO chatRoomCreateDTO);

    void deleteChatRoom(long chatRoomId);
}
