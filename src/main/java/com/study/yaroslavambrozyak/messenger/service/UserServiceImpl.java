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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static String UPLOAD_FOLDER = System.getProperty("catalina.home");

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

    /**
     * This method is used to get user entity from database by id.
     * If the user is not found throws exception
     * @param id user`s id
     * @return user entity
     */
    @Override
    public User getUserEntity(long id) {
        return Optional.ofNullable(userRepository.findOne(id))
                .orElseThrow(() -> new UserNotFoundException(
                        messageSource.getMessage("exception.user.not-found-by-id",
                                new Object[]{id}, LocaleContextHolder.getLocale())));
    }

    /**
     * This method is used to get current user entity from database by auth token.
     * If the user is not found throws exception
     * @return current user entity
     */
    @Override
    public User getCurrentUserEntity() {
        return Optional.ofNullable(
                userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .orElseThrow(() -> new UserNotFoundException(
                        messageSource.getMessage("exception.current-user.not-found", null,
                                LocaleContextHolder.getLocale())));
    }

    /**
     * This method is used to map current user entity to user data transferred object
     * @return current userDTO
     */
    @Transactional(readOnly = true)
    @Override
    public UserDTO getCurrentUser() {
        return modelMapper.map(getCurrentUserEntity(), UserDTO.class);
    }

    /**
     * This method is used to map user entity to user data transferred object
     * @return userDTO
     */
    @Transactional(readOnly = true)
    @Override
    public UserDTO getUserById(long id) {
        return modelMapper.map(getUserEntity(id), UserDTO.class);
    }

    /**
     * This method checks if there is a user with the transferred email address.
     * If user is exists throws exception. Else create new user
     * @param registrationDTO user`s data
     */
    @Override
    public void createUser(RegistrationDTO registrationDTO) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(registrationDTO.getEmail()));
        if (user.isPresent()) throw new UserAlreadyExists(messageSource.getMessage("exception.user.already-exist",
                new Object[]{registrationDTO.getEmail()}, LocaleContextHolder.getLocale()));
        User newUser = modelMapper.map(registrationDTO,User.class);
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        userRepository.save(newUser);
    }

    /**
     * This method is used to update user entity
     * @param userUpdateDTO data for update
     */
    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        User userToUpdate = getCurrentUserEntity();
        NullAwareBeanUtil.copyProperties(userUpdateDTO, userToUpdate);
        userRepository.save(userToUpdate);
    }

    /**
     * This method is used to delete current user entity
     */
    @Override
    public void deleteUser() {
        userRepository.delete(getCurrentUserEntity());
    }

    /**
     * This method is used to get all user`s chats and map their to data transferred object
     * @param pageable page
     * @return list of chats DTO
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ChatRoomDTO> getUserChats(Pageable pageable) {
        return userRepository.getChatRoom(getCurrentUserEntity().getId(), pageable)
                .map(chatRoom -> modelMapper.map(chatRoom, ChatRoomDTO.class));
    }

    /**
     * This method checks if there is a user with transferred id in friend request list.
     * If user is exists delete him from friend request list and add to friend list.
     * If user is not exists throws exception.
     * @param friendId friend`s id
     */
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

    /**
     * This method check if there is a user with transferred id in friend list.
     * If user is exists delete him. Else throws exception
     * @param friendId friend`s id
     */
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

    /**
     * This method is used to get all user`s friends by user id
     * @param id user`s id
     * @param pageable page
     * @return list of friends
     */
    @Transactional(readOnly = true)
    @Override
    public Page<UserDTO> getUserFriends(long id, Pageable pageable) {
        return userRepository.getUserFriends(id, pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    /**
     * This method is used to get all current user`s friends
     * @param pageable page
     * @return userDTO
     */
    @Transactional(readOnly = true)
    @Override
    public Page<UserDTO> getUserFriends(Pageable pageable) {
        return getUserFriends(getCurrentUserEntity().getId(), pageable);
    }

    /**
     * This method is used to get all current user`s friend requests
     * @param pageable page
     * @return userDTO
     */
    @Transactional(readOnly = true)
    @Override
    public Page<UserDTO> getUserFriendRequest(Pageable pageable) {
        return userRepository.getUserFriendRequest(getCurrentUserEntity().getId(), pageable)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    /**
     * This method checks if there is a user in friend list and it is not request to yourself.
     * If both conditions are met make friend request.
     * Throws exception if request to yourself
     * @param friendId friend`s id
     */
    @Override
    public void friendRequest(long friendId) {
        User current = getCurrentUserEntity();
        boolean isNotExist = current.getFriends()
                .parallelStream()
                .noneMatch(friend -> friend.getId() == friendId);
        if (friendId == current.getId() && isNotExist)
            throw new SameUserException(messageSource.getMessage("exception.user.self-friend-req"
                    , null, LocaleContextHolder.getLocale()));
        User user = getUserEntity(friendId);
        user.getFriendsReq().add(current);
        userRepository.save(user);
    }

    /**
     * This method is used to upload user`s profile picture
     * @param multipartFile file
     * @throws IOException if file is empty
     */
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

    /**
     * This method is used to get user`s profile picture
     * @param id user`s id
     * @return user picture
     * @throws IOException
     */
    @Override
    public Resource loadPicture(long id) throws IOException {
        String imagePath = getCurrentUserEntity().getImagePath();
        Path path = Paths.get(UPLOAD_FOLDER + imagePath);
        return new ByteArrayResource(Files.readAllBytes(path));
    }

    /**
     * This method search users by specific parameters
     * @param specification search params
     * @param pageable page
     * @return list of users
     */
    @Transactional(readOnly = true)
    @Override
    public Page<UserDTO> searchUsers(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification,pageable)
                .map(user -> modelMapper.map(user,UserDTO.class));
    }
}
