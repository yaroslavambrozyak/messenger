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

    /**
     * This method check if there is a chat room and if there is current user in this chat room.
     * If chat room is not exists or user is not in chat throws exception.
     * @param id chat`s id
     * @return chat room
     */
    @Override
    public ChatRoom getChatRoomEntity(long id) {
        ChatRoom chatRoom = Optional.ofNullable(chatRoomRepository.findOne(id))
                .orElseThrow(() -> new ChatRoomNotFoundException(messageSource.getMessage("exception.chat-room.not-found-by-id",
                        new Object[]{id}, LocaleContextHolder.getLocale())));
        if (!checkIsUserExist(chatRoom))
            throw new UserNotFoundException(messageSource.getMessage("exception.chat-room.current-user-not-found",
                    null, LocaleContextHolder.getLocale()));
        return chatRoom;
    }

    /**
     * This method creates new chat room
     * @param chatRoomCreateDTO chat`s data
     * @return chat`s id
     */
    @Override
    public long createChatRoom(ChatRoomCreateDTO chatRoomCreateDTO) {
        ChatRoom chatRoom = modelMapper.map(chatRoomCreateDTO, ChatRoom.class);
        User user = userService.getCurrentUserEntity();
        chatRoom.getUsersInRoom().add(user);
        return saveChatRoom(chatRoom).getId();
    }

    /**
     * This method is used to update chat room by id
     * @param chatRoomId chat`s id
     * @param chatRoomCreateDTO chat`s data
     */
    @Override
    public void updateChatRoom(long chatRoomId, ChatRoomCreateDTO chatRoomCreateDTO) {
        ChatRoom chatRoom = getChatRoomEntity(chatRoomId);
        chatRoom.setName(chatRoomCreateDTO.getName());
        saveChatRoom(chatRoom);
    }

    /**
     * This method is used to delete chat room
     * @param chatRoomId chat`s id
     */
    @Override
    public void deleteChatRoom(long chatRoomId) {
        chatRoomRepository.delete(chatRoomId);
    }

    /**
     * This method is used to save chat room and update last activity time
     * @param chatRoom chat entity
     * @return chat room entity
     */
    @Override
    public ChatRoom saveChatRoom(ChatRoom chatRoom) {
        chatRoom.setLastActivity(LocalDateTime.now());
        return chatRoomRepository.save(chatRoom);
    }

    /**
     * This method is used to get chat room and map it to data transferred object
     * @param id chat`s id
     * @return chat room data
     */
    @Transactional(readOnly = true)
    @Override
    public ChatRoomDTO getChatRoom(long id) {
        return modelMapper.map(getChatRoomEntity(id), ChatRoomDTO.class);
    }

    /**
     * This method is used to add user to chat
     * @param chatRoomId chat`s id
     * @param userId user`s id
     */
    @Override
    public void addUserToChat(long chatRoomId, long userId) {
        ChatRoom chatRoom = getChatRoomEntity(chatRoomId);
        User user = userService.getUserEntity(userId);
        chatRoom.getUsersInRoom().add(user);
        saveChatRoom(chatRoom);
    }

    /**
     * This method is used to delete user from chat room
     * @param chatRoomId from which chat you delete user
     * @param userId which user you delete
     */
    @Override
    public void deleteUserFromChat(long chatRoomId, long userId) {
        ChatRoom chatRoom = getChatRoomEntity(chatRoomId);
        User user = userService.getUserEntity(userId);
        chatRoom.getUsersInRoom().remove(user);
        saveChatRoom(chatRoom);
    }

    /**
     * This method is used to get all chat`s messages
     * @param id chat`s id
     * @param pageable page
     * @return list of messages
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MessageDateDTO> getChatMessages(long id, Pageable pageable) {
        return chatRoomRepository.getChatRoomMessages(id, pageable)
                .map(message -> modelMapper.map(message, MessageDateDTO.class));
    }

    /**
     * This method is used to get all users in chat
     * @param id chat`s id
     * @param pageable page
     * @return list of users
     */
    @Transactional(readOnly = true)
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
