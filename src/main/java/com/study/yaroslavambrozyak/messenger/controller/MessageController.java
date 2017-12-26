package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public MessageDTO message(MessageDTO messageDTO) throws InterruptedException {
        Thread.sleep(1000);
        return messageDTO;
    }

}
