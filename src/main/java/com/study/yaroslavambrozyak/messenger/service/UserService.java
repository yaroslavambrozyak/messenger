package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.UserAlreadyExists;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;

import java.util.Set;

public interface UserService {

    User getUserEntity(Long id) throws UserNotFoundException;
    User getCurrentUser();
    UserDTO getUserById(Long id) throws UserNotFoundException;
    UserDTO getUserByUserName(String userName) throws UserNotFoundException;
    void createUser(RegistrationDTO registrationDTO) throws UserAlreadyExists;
    void updateUser(UserUpdateDTO user, long id) throws UserNotFoundException;
    void deleteUser(Long id) throws UserNotFoundException;
    Long getUserIdByName(String name);

    Set<ChatRoomDTO> getUserChats(Long id) throws UserNotFoundException;
}
