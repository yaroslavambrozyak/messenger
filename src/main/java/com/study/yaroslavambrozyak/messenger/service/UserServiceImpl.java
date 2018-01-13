package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.UserAlreadyExists;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.repository.UserRepository;
import com.study.yaroslavambrozyak.messenger.util.NullAwareBeanUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User getUserEntity(long id) {
        return Optional.ofNullable(userRepository.findOne(id))
                .orElseThrow(() -> new UserNotFoundException("Cant find user with id: " + id));
    }

    @Override
    public User getCurrentUserEntity() {
        return Optional.ofNullable(
                userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new UserNotFoundException("Cant find user"));
    }

    @Override
    public UserDTO getCurrentUser() {
        return modelMapper.map(getCurrentUserEntity(), UserDTO.class);
    }

    @Override
    public UserDTO getUserById(long id) {
        return modelMapper.map(getUserEntity(id), UserDTO.class);
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        User user = Optional.ofNullable(userRepository.findByName(userName))
                .orElseThrow(() -> new UserNotFoundException("Cant find user with name: " + userName));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void createUser(RegistrationDTO registrationDTO) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(registrationDTO.getEmail()));
        if (user.isPresent()) throw new UserAlreadyExists("User with email "
                + registrationDTO.getEmail() + " already exist");
        userRepository.save(modelMapper.map(registrationDTO, User.class));
    }

    @Override
    public void updateUser(UserUpdateDTO user, long id) {
        User userToUpdate = getUserEntity(id);
        if (userToUpdate.getId() == getCurrentUserEntity().getId()) {
            NullAwareBeanUtil.copyProperties(user, userToUpdate);
            userToUpdate.setId(id);
            userRepository.save(userToUpdate);
        }
    }

    @Override
    public void deleteUser(long id) {
        User userToDelete = getUserEntity(id);
        User currentUser = getCurrentUserEntity();
        if (userToDelete.getId() == currentUser.getId())
            userRepository.delete(id);
    }

    @Override
    public long getUserIdByName(String name) {
        return userRepository.getUserId(name);
    }

    @Override
    public Page<ChatRoomDTO> getUserChats(long id, Pageable pageable) {
        return userRepository.getChatRoom(id, pageable)
                .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomDTO.class));

    }

    @Override
    public void addFriend(long id, long friendId) {
        User user = getCurrentUserEntity();
        if (user.getId() == id) {
            User friend = getUserEntity(friendId);
            if (user.getFriendsReq().contains(friend)) {
                user.getFriends().add(friend);
                userRepository.save(user);
            }
        }
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        User user = getCurrentUserEntity();
        if (user.getId() == id) {
            User friend = getUserEntity(friendId);
            user.getFriends().remove(friend);
            userRepository.save(user);
        }
    }

    @Override
    public Page<UserDTO> getUserFriends(long id, Pageable pageable) {
        return userRepository.getUserFriends(id, pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public Page<UserDTO> getUserFriendRequest(long id, Pageable pageable) {
        return userRepository.getUserFriendRequest(id, pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public void friendRequest(long id, long friendId) {
        User user = getUserEntity(friendId);
        User current = getCurrentUserEntity();
        user.getFriendsReq().add(current);
        userRepository.save(user);
    }
}
