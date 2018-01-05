package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User getUserById(Long id) {
        User user = userRepository.getOne(id);
        if (user==null) throw new UserNotFoundException("Can`t find user with id " + id);
        return userRepository.getOne(id);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.getByName(userName);
    }

    @Override
    public void createUser(RegistrationDTO registrationDTO) {
        userRepository.save(modelMapper.map(registrationDTO,User.class));
    }

    @Override
    public void updateUser(User user) {
        User updatableUser = getUserById(user.getId());
        updatableUser.setName(user.getName());
        updatableUser.setSurName(user.getSurName());
        userRepository.save(updatableUser);
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
    public Set<ChatRoom> getUserChats(Long id) {
        return getUserById(id).getUserChatRooms();
    }
}
