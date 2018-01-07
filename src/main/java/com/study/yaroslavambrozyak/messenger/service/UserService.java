package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.User;

import java.util.Set;

public interface UserService {

    User getUserEntity(Long id);
    UserDTO getUserById(Long id);
    UserDTO getUserByUserName(String userName);
    void createUser(RegistrationDTO registrationDTO);
    void updateUser(UserUpdateDTO user, long id);
    void deleteUser(Long id);
    Long getUserIdByName(String name);

    Set<ChatRoomDTO> getUserChats(Long id);
}
