package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;

public interface MessageService {

    void saveMessage(MessageDTO messageDTO,long chatId) throws UserNotFoundException, ChatRoomNotFoundException;

}
