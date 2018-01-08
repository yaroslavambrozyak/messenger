package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.entity.ChatRoom;
import com.study.yaroslavambrozyak.messenger.entity.Message;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.repository.ChatRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ChatRoom getChatRoomEntity(long id) throws ChatRoomNotFoundException {
        return Optional.ofNullable(chatRoomRepository.findOne(id))
                .orElseThrow(() -> new ChatRoomNotFoundException("Cant find chat with id: " + id));
    }

    @Override
    public ChatRoomDTO getChatRoom(long id) throws ChatRoomNotFoundException {
        return modelMapper.map(getChatRoomEntity(id), ChatRoomDTO.class);
    }

    @Override
    public void createChatRoom(ChatRoomDTO chatRoomDTO) throws UserNotFoundException {
        ChatRoom chatRoom = modelMapper.map(chatRoomDTO, ChatRoom.class);
        User user = userService.getUserEntity(chatRoomDTO.getCreatorId());
        chatRoom.getUsersInRoom().add(user);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void addUserToChat(long chatRoomId, long userId) throws UserNotFoundException, ChatRoomNotFoundException {
        User user = userService.getUserEntity(userId);
        ChatRoom chatRoom = getChatRoomEntity(userId);
        chatRoom.getUsersInRoom().add(user);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public Set<MessageDTO> getChatMessages(long id) throws ChatRoomNotFoundException {
        return getChatRoomEntity(id)
                .getMessages()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserDTO> getUsersInChat(long id) throws ChatRoomNotFoundException {
        return getChatRoomEntity(id)
                .getUsersInRoom()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toSet());
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setText(message.getText());
        messageDTO.setUserId(message.getUser().getId());
        return messageDTO;
    }

}
