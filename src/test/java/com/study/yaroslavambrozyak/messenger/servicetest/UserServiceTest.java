package com.study.yaroslavambrozyak.messenger.servicetest;

import com.study.yaroslavambrozyak.messenger.dto.RegistrationDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.UserAlreadyExists;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.repository.UserRepository;
import com.study.yaroslavambrozyak.messenger.service.UserService;
import com.study.yaroslavambrozyak.messenger.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MessageSource messageSource;

    @Before
    public void init() {
        userService = new UserServiceImpl(userRepository, modelMapper,messageSource);
    }

    //TODO try user eq!
    @Test
    public void getUserEntitySuccess() {
        User testUser = new User();
        testUser.setId(1);
        when(userRepository.findOne(1L)).thenReturn(testUser);
        User rUser = userService.getUserEntity(1L);
        assertEquals(1, rUser.getId());
        verify(userRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getUserEntityNotFound() {
        when(userRepository.findOne(1L)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserEntity(1L));
        verify(userRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getUserDTOSuccess() {
        User testUser = new User();
        testUser.setId(1);
        testUser.setName("testName");

        when(userRepository.findOne(1L)).thenReturn(testUser);

        UserDTO userDTO = userService.getUserById(1L);
        assertEquals(1L, userDTO.getId());
        assertEquals("testName", userDTO.getName());
        verify(userRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getUserDTONotFound() {
        when(userRepository.findOne(1L)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void createUserSuccess() {
        RegistrationDTO testRegistrationDTO = new RegistrationDTO("name", "sur"
                , "email", "1111",new Date());
        User user = modelMapper.map(testRegistrationDTO, User.class);

        when(userRepository.save(user)).thenReturn(user);

        userService.createUser(testRegistrationDTO);
        verify(userRepository, times(1)).findByEmail("email");
        verify(userRepository, times(1)).save(refEq(user));
        verifyZeroInteractions(userRepository);
    }

    @Test
    public void createUserAlreadyExist() {
        RegistrationDTO testRegistrationDTO = new RegistrationDTO("name", "sur"
                , "email", "1111", new Date());

        when(userRepository.findByEmail("email")).thenReturn(new User());

        assertThrows(UserAlreadyExists.class, () -> userService.createUser(testRegistrationDTO));
        verify(userRepository, times(1)).findByEmail("email");
        verifyNoMoreInteractions(userRepository);
    }

    // NPE NullAwareBeans
    @Test
    public void updateUserSuccess() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        User user = new User();

        when(userRepository.save(user)).thenReturn(user);

        userService.updateUser(userUpdateDTO);

        verify(userRepository, times(1)).save(refEq(user));
        verifyNoMoreInteractions(userRepository);
    }

    //fix NPE ---
    @Test
    public void deleteUserSuccess() {
        doNothing().when(userRepository).delete(1L);

        userService.deleteUser();

        verify(userRepository, times(1)).delete(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void addFriendSuccess() {
       /* User testUser = new User();
        User testFriendReqUser = new User();
        testFriendReqUser.setId(2);
        testUser.getFriendsReq().add(testFriendReqUser);
        when(userRepository.findByEmail("e")).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);

        userService.addFriend(2);

        verify(userRepository,times(1)).findByEmail("e");
        verify(userRepository,times(1)).save(testUser);
        verifyNoMoreInteractions(userRepository);*/
    }
}
