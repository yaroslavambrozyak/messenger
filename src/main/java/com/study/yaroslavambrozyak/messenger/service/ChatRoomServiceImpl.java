package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomCreateDTO;
import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.repository.ChatRoomRepository;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public ChatRoom getChatRoomEntity(long id) throws ChatRoomNotFoundException, UserNotFoundException {
        ChatRoom chatRoom = Optional.ofNullable(chatRoomRepository.findOne(id))
                .orElseThrow(() -> new ChatRoomNotFoundException("Cant find chat with id: " + id));
        if (!checkIsUserExist(chatRoom))
            throw new UserNotFoundException("User not in room");
        return chatRoom;
    }

    @Override
    public ChatRoomDTO getChatRoom(long id) throws ChatRoomNotFoundException, UserNotFoundException {
        return modelMapper.map(getChatRoomEntity(id), ChatRoomDTO.class);
    }

    @Override
    public long createChatRoom(ChatRoomCreateDTO chatRoomCreateDTO) throws UserNotFoundException {
        ChatRoom chatRoom = modelMapper.map(chatRoomCreateDTO, ChatRoom.class);
        User user = userService.getCurrentUser();
        chatRoom.getUsersInRoom().add(user);
        return chatRoomRepository.save(chatRoom).getId();
    }

    @Override
    public void addUserToChat(long chatRoomId, long userId) throws UserNotFoundException, ChatRoomNotFoundException {
        ChatRoom chatRoom = getChatRoomEntity(chatRoomId);
        User user = userService.getUserEntity(userId);
        chatRoom.getUsersInRoom().add(user);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public Page<MessageDTO> getChatMessages(long id, Pageable pageable) throws ChatRoomNotFoundException, UserNotFoundException {
        return chatRoomRepository.getChatRoomMessages(id, pageable)
                .map(message -> modelMapper.map(message, MessageDTO.class));
    }

    @Override
    public Page<UserDTO> getUsersInChat(long id, Pageable pageable) throws ChatRoomNotFoundException, UserNotFoundException {
        return chatRoomRepository.getUsersInChatRoom(id,pageable)
                .map(user -> modelMapper.map(user,UserDTO.class));
    }


    private boolean checkIsUserExist(ChatRoom chatRoom) {
        User user = userService.getCurrentUser();
        return chatRoom.getUsersInRoom()
                .parallelStream()
                .anyMatch(userIn -> userIn.getId() == user.getId());
    }

}
