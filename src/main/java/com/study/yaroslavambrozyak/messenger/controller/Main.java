package com.study.yaroslavambrozyak.messenger.controller;

import com.study.yaroslavambrozyak.messenger.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class Main {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Message message(Message message) throws InterruptedException {
        Thread.sleep(1000);
        return message;
    }

}
