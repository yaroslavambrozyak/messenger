package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;

public interface MessageService {

    void saveMessage(MessageDTO messageDTO,long chatId);

}
