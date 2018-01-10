package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.exception.UserAlreadyExists;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.repository.UserRepository;
import com.study.yaroslavambrozyak.messenger.util.NullAwareBeanUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User getUserEntity(Long id) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findOne(id))
                .orElseThrow(() -> new UserNotFoundException("Cant find user with id: " + id));
    }

    @Override
    public User getCurrentUser() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public UserDTO getUserById(Long id) throws UserNotFoundException {
        return modelMapper.map(getUserEntity(id), UserDTO.class);
    }

    @Override
    public UserDTO getUserByUserName(String userName) throws UserNotFoundException {
        User user = Optional.ofNullable(userRepository.findByName(userName))
                .orElseThrow(() -> new UserNotFoundException("Cant find user with name: " + userName));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void createUser(RegistrationDTO registrationDTO) throws UserAlreadyExists {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(registrationDTO.getEmail()));
        if (user.isPresent()) throw new UserAlreadyExists("User with email "
                + registrationDTO.getEmail() + " already exist");
        userRepository.save(modelMapper.map(registrationDTO, User.class));
    }

    @Override
    public void updateUser(UserUpdateDTO user, long id) throws UserNotFoundException {
        User userToUpdate = getUserEntity(id);
        if (userToUpdate.getId() == getCurrentUser().getId()) {
            NullAwareBeanUtil.copyProperties(user, userToUpdate);
            userToUpdate.setId(id);
            userRepository.save(userToUpdate);
        }
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        User userToDelete = getUserEntity(id);
        User currentUser = getCurrentUser();
        if (userToDelete.getId()==currentUser.getId())
            userRepository.delete(id);
    }

    @Override
    public Long getUserIdByName(String name) {
        return userRepository.getUserId(name);
    }

    @Override
    public Set<ChatRoomDTO> getUserChats(Long id) throws UserNotFoundException {
        return getUserEntity(id)
                .getUserChatRooms()
                .stream()
                .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomDTO.class))
                .collect(Collectors.toSet());
    }
}
