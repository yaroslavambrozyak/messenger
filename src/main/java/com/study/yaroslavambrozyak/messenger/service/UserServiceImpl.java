package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.repository.UserRepository;
import com.study.yaroslavambrozyak.messenger.util.NullAwareBeanUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User getUserEntity(Long id) {
        User user = userRepository.getOne(id);
        if (user==null){
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.getOne(id);
        return modelMapper.map(userRepository.getOne(id), UserDTO.class);
    }

    @Override
    public UserDTO getUserByUserName(String userName) {
        return modelMapper.map(userRepository.getByName(userName), UserDTO.class);
    }

    @Override
    public void createUser(RegistrationDTO registrationDTO) {
        userRepository.save(modelMapper.map(registrationDTO, User.class));
    }

    @Override
    public void updateUser(UserUpdateDTO user, long id) {
        User userToUpdate = userRepository.getOne(id);
        NullAwareBeanUtil.copyProperties(user, userToUpdate);
        userToUpdate.setId(id);
        userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public Long getUserIdByName(String name) {
        return userRepository.getUserId(name);
    }

    @Override
    public Set<ChatRoomDTO> getUserChats(Long id) {
        return userRepository.getOne(id)
                .getUserChatRooms()
                .stream()
                .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomDTO.class))
                .collect(Collectors.toSet());
    }
}
