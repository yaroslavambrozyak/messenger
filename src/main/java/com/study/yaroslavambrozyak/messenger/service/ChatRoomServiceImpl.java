package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.*;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.repository.ChatRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {


    private ChatRoomRepository chatRoomRepository;
    private UserService userService;
    private ModelMapper modelMapper;
    private MessageSource messageSource;

    @Autowired
    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, UserService userService,
                               ModelMapper modelMapper, MessageSource messageSource) {
        this.chatRoomRepository = chatRoomRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
    public ChatRoom getChatRoomEntity(long id) {
        ChatRoom chatRoom = Optional.ofNullable(chatRoomRepository.findOne(id))
                .orElseThrow(() -> new ChatRoomNotFoundException(messageSource.getMessage("exception.chat-room.not-found-by-id",
                        new Object[]{id}, LocaleContextHolder.getLocale())));
        if (!checkIsUserExist(chatRoom))
            throw new UserNotFoundException(messageSource.getMessage("exception.chat-room.current-user-not-found",
                    null, LocaleContextHolder.getLocale()));
        return chatRoom;
    }

    @Override
    public void deleteUserFromChat(long chatRoomId, long userId) {
        ChatRoom chatRoom = getChatRoomEntity(chatRoomId);
        User user = userService.getUserEntity(userId);
        chatRoom.getUsersInRoom().remove(user);
        saveChatRoom(chatRoom);
    }

    @Override
    public void updateChatRoom(long chatRoomId, ChatRoomCreateDTO chatRoomCreateDTO) {
        ChatRoom chatRoom = getChatRoomEntity(chatRoomId);
        chatRoom.setName(chatRoomCreateDTO.getName());
        saveChatRoom(chatRoom);
    }

    @Override
    public void deleteChatRoom(long chatRoomId) {
        chatRoomRepository.delete(chatRoomId);
    }

    @Override
    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        chatRoom.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public ChatRoomDTO getChatRoom(long id) {
        return modelMapper.map(getChatRoomEntity(id), ChatRoomDTO.class);
    }

    @Override
    public long createChatRoom(ChatRoomCreateDTO chatRoomCreateDTO) {
        ChatRoom chatRoom = modelMapper.map(chatRoomCreateDTO, ChatRoom.class);
        User user = userService.getCurrentUserEntity();
        chatRoom.getUsersInRoom().add(user);
        return saveChatRoom(chatRoom).getId();
    }

    @Override
    public void addUserToChat(long chatRoomId, long userId) {
        ChatRoom chatRoom = getChatRoomEntity(chatRoomId);
        User user = userService.getUserEntity(userId);
        chatRoom.getUsersInRoom().add(user);
        saveChatRoom(chatRoom);
    }

    @Override
    public Page<MessageDateDTO> getChatMessages(long id, Pageable pageable) {
        return chatRoomRepository.getChatRoomMessages(id, pageable)
                .map(message -> modelMapper.map(message, MessageDateDTO.class));
    }

    @Override
    public Page<UserDTO> getUsersInChat(long id, Pageable pageable) {
        return chatRoomRepository.getUsersInChatRoom(id, pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    private boolean checkIsUserExist(ChatRoom chatRoom) {
        User user = userService.getCurrentUserEntity();
        return chatRoom.getUsersInRoom()
                .parallelStream()
                .anyMatch(userIn -> userIn.getId() == user.getId());
    }

}
