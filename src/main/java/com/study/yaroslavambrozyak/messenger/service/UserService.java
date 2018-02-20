package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.UserAlreadyExists;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface UserService {

    User getUserEntity(long id);

    User getCurrentUserEntity();

    UserDTO getCurrentUser();

    UserDTO getUserById(long id);

    void createUser(RegistrationDTO registrationDTO);

    void updateUser(UserUpdateDTO user);

    void deleteUser();

    Page<ChatRoomDTO> getUserChats(Pageable pageable);

    void addFriend(long friendId);

    void deleteFriend(long friendId);

    Page<UserDTO> getUserFriends(long id, Pageable pageable);

    Page<UserDTO> getUserFriends(Pageable pageable);

    Page<UserDTO> getUserFriendRequest(Pageable pageable);

    void friendRequest(long friendId);

    void uploadPicture(MultipartFile multipartFile) throws IOException;

    Resource loadPicture(long id) throws IOException;

    Page<UserDTO> searchUsers(Specification<User> specification, Pageable pageable);
}
