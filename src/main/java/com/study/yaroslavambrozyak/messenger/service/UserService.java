package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.User;

import java.util.Set;

public interface UserService {

    User getUserById(Long id);
    void createUser(RegistrationDTO registrationDTO);
    void updateUser(User user);
    void deleteUser(Long id);

    Set<ChatRoom> getUserChats(Long id);
}