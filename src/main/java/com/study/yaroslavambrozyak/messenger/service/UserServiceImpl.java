package com.study.yaroslavambrozyak.messenger.service;

import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.SameUserException;
import com.study.yaroslavambrozyak.messenger.exception.UserAlreadyExists;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.repository.UserRepository;
import com.study.yaroslavambrozyak.messenger.util.NullAwareBeanUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private MessageSource messageSource;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper
            , MessageSource messageSource, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserEntity(long id) {
        return Optional.ofNullable(userRepository.findOne(id))
                .orElseThrow(() -> new UserNotFoundException(
                        messageSource.getMessage("exception.user.not-found-by-id",
                                new Object[]{id}, LocaleContextHolder.getLocale())));
    }

    @Override
    public User getCurrentUserEntity() {
        return Optional.ofNullable(
                userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new UserNotFoundException(
                        messageSource.getMessage("exception.current-user.not-found", null,
                                LocaleContextHolder.getLocale())));
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
                .orElseThrow(() -> new UserNotFoundException(
                        messageSource.getMessage("exception.user.not-found-by-name",
                                new Object[]{userName}, LocaleContextHolder.getLocale())));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void createUser(RegistrationDTO registrationDTO) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(registrationDTO.getEmail()));
        if (user.isPresent()) throw new UserAlreadyExists(messageSource.getMessage("exception.user.already-exist",
                new Object[]{registrationDTO.getEmail()}, LocaleContextHolder.getLocale()));
        User newUser = modelMapper.map(registrationDTO,User.class);
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public void updateUser(UserUpdateDTO user) {
        User userToUpdate = getCurrentUserEntity();
        NullAwareBeanUtil.copyProperties(user, userToUpdate);
        userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser() {
        userRepository.delete(getCurrentUserEntity());
    }

    @Override
    public long getUserIdByName(String name) {
        return userRepository.getUserId(name);
    }

    @Override
    public Page<ChatRoomDTO> getUserChats(Pageable pageable) {
        return userRepository.getChatRoom(getCurrentUserEntity().getId(), pageable)
                .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomDTO.class));
    }


    @Override
    public void addFriend(long friendId) {
        User user = getCurrentUserEntity();
        boolean isContains = user.getFriendsReq()
                .parallelStream()
                .anyMatch(friendReq -> friendReq.getId() == friendId);
        if (isContains) {
            User friend = getUserEntity(friendId);
            user.getFriends().add(friend);
            user.getFriendsReq().remove(friend);
            friend.getFriends().add(user);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException(messageSource.getMessage("exception.user.friend-req.not-found",
                    new Object[]{friendId}, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public void deleteFriend(long friendId) {
        User user = getCurrentUserEntity();
        boolean isFriendContains = user.getFriends()
                .parallelStream()
                .anyMatch(friend -> friend.getId() == friendId);
        if (isFriendContains) {
            user.getFriends().remove(getUserEntity(friendId));
            userRepository.save(user);
        } else {
            throw new UserNotFoundException(messageSource.getMessage("exception.user.friend.not-found",
                    new Object[]{friendId}, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public Page<UserDTO> getUserFriends(long id, Pageable pageable) {
        return userRepository.getUserFriends(id, pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public Page<UserDTO> getUserFriends(Pageable pageable) {
        return getUserFriends(getCurrentUserEntity().getId(), pageable);
    }

    @Override
    public Page<UserDTO> getUserFriendRequest(Pageable pageable) {
        return userRepository.getUserFriendRequest(getCurrentUserEntity().getId(), pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public void friendRequest(long friendId) {
        User current = getCurrentUserEntity();
        boolean isNotExist = current.getFriends().stream().noneMatch(friend -> friend.getId() == friendId);
        if (friendId == current.getId() && isNotExist)
            throw new SameUserException(messageSource.getMessage("exception.user.self-friend-req"
                    , null, LocaleContextHolder.getLocale()));
        User user = getUserEntity(friendId);
        user.getFriendsReq().add(current);
        userRepository.save(user);
    }

    //??????????
    private static String UPLOAD_FOLDER = System.getProperty("catalina.home");

    @Override
    public void uploadPicture(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            throw new RuntimeException();
        User user = getCurrentUserEntity();
        byte[] bytes = multipartFile.getBytes();
        String type = multipartFile.getContentType().split("/")[1];
        String imageName = "pic-" + user.getId() + "." + type;
        Path path = Paths.get(UPLOAD_FOLDER + imageName);
        Files.write(path, bytes);
        user.setImagePath(imageName);
        userRepository.save(user);
    }

    @Override
    public Resource loadPicture(long id) throws IOException {
        String imagePath = getCurrentUserEntity().getImagePath();
        Path path = Paths.get(UPLOAD_FOLDER + imagePath);
        return new ByteArrayResource(Files.readAllBytes(path));
    }

    @Override
    public Page<UserDTO> searchUsers(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification,pageable)
                .map(user -> modelMapper.map(user,UserDTO.class));
    }
}
