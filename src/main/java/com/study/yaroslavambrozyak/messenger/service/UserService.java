package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.UserAlreadyExists;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {

    User getUserEntity(long id);

    User getCurrentUserEntity();

    UserDTO getCurrentUser();

    UserDTO getUserById(long id);

    UserDTO getUserByUserName(String userName);

    void createUser(RegistrationDTO registrationDTO);

    void updateUser(UserUpdateDTO user, long id);

    void deleteUser(long id);

    long getUserIdByName(String name);

    Page<ChatRoomDTO> getUserChats(long id, Pageable pageable);

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    Page<UserDTO> getUserFriends(long id, Pageable pageable);

    Page<UserDTO> getUserFriendRequest(long id, Pageable pageable);

    void friendRequest(long id, long friendId);
}
