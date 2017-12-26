package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void saveMessage(MessageDTO messageDTO) {

    }
}
