package com.study.yaroslavambrozyak.messenger.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.yaroslavambrozyak.messenger.TestUtil;
import com.study.yaroslavambrozyak.messenger.controller.ExceptionController;
import com.study.yaroslavambrozyak.messenger.controller.UserController;
import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.service.TokenAuthenticationService;
import com.study.yaroslavambrozyak.messenger.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private ExceptionController exceptionController;
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    private MockMvc mockMvc;
    private String token;
    private final String AUTHORIZATION = "Authorization";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        token = TokenAuthenticationService.createToken("testUser");
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(exceptionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {
        UserDTO testUserDTO = new UserDTO();
        testUserDTO.setId(1);
        testUserDTO.setName("testName");
        testUserDTO.setSurName("testSurName");

        when(userController.getUser(1)).thenReturn(testUserDTO);

        mockMvc.perform(get("/app/user/1").header(AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("testName")))
                .andExpect(jsonPath("$.surName", is("testSurName")));

        verify(userService, times(1)).getUserById(1);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        when(userController.getUser(1)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/app/user/1").header(AUTHORIZATION, token))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testUpdateUserSuccess() throws Exception {
        UserUpdateDTO testUserUpdateDTO = new UserUpdateDTO();
        doNothing().when(userService).updateUser(testUserUpdateDTO);

        mockMvc.perform(put("/app/user")
                .header(AUTHORIZATION, token)
                .content(TestUtil.asJsonString(testUserUpdateDTO)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(refEq(testUserUpdateDTO));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        doNothing().when(userService).deleteUser();

        mockMvc.perform(delete("/app/user").header(AUTHORIZATION, token))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testDeleteNotFound() throws Exception{
        doThrow(new UserNotFoundException()).when(userService).deleteUser();

        mockMvc.perform(delete("/app/user").header(AUTHORIZATION,token))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetUserFriendsReqSuccess() throws Exception{
        List<UserDTO> testList = Arrays.asList(
                new UserDTO(1,"testName","testSurname")
                ,new UserDTO(2,"testName","testSurname"));
        PageRequest pageRequest = new PageRequest(0,2);
        Page<UserDTO> testPage = new PageImpl<>(testList,pageRequest,2);

        when(userService.getUserFriendRequest(pageRequest)).thenReturn(testPage);

       mockMvc.perform(get("/app/user/friend.request?page=0&size=2")
                .header(AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].id",is(1)))
                .andExpect(jsonPath("$.content[1].id",is(2)));

        verify(userService,times(1)).getUserFriendRequest(pageRequest);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testMakeFriendRequestSuccess() throws Exception {
        doNothing().when(userService).friendRequest(2);

        mockMvc.perform(post("/app/user/friend.request/2").header(AUTHORIZATION,token))
                .andExpect(status().isOk());

        verify(userService,times(1)).friendRequest(2);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetFriendsRequestSuccess() throws Exception {
        List<UserDTO> testList = Arrays.asList(
                new UserDTO(1,"testName","testSurname")
                ,new UserDTO(2,"testName","testSurname"));
        PageRequest pageRequest = new PageRequest(0,2);
        Page<UserDTO> testPage = new PageImpl<>(testList,pageRequest,2);

        when(userService.getUserFriends(pageRequest)).thenReturn(testPage);

        mockMvc.perform(get("/app/user/friends?page=0&size=2")
                .header(AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].id",is(1)))
                .andExpect(jsonPath("$.content[1].id",is(2)));

        verify(userService,times(1)).getUserFriends(pageRequest);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testAddUserToFriendsSuccess() throws Exception{
        doNothing().when(userService).addFriend(2);

        mockMvc.perform(post("/app/user/add/2")
                .header(AUTHORIZATION,token))
                .andExpect(status().isOk());

        verify(userService,times(1)).addFriend(2);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testAddUserToFriendsNotFound() throws Exception{
        doThrow(new UserNotFoundException()).when(userService).addFriend(3);

        mockMvc.perform(post("/app/user/add/3")
                .header(AUTHORIZATION,token))
                .andExpect(status().isNotFound());

        verify(userService,times(1)).addFriend(3);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testDeleteUserFromFriendsSuccess() throws Exception{
        doNothing().when(userService).deleteFriend(2);

        mockMvc.perform(delete("/app/user/delete/2")
                .header(AUTHORIZATION,token))
                .andExpect(status().isOk());

        verify(userService,times(1)).deleteFriend(2);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testDeleteUserFromFriendsNotFound() throws Exception{
        doThrow(new UserNotFoundException()).when(userService).deleteFriend(2);

        mockMvc.perform(delete("/app/user/delete/2")
                .header(AUTHORIZATION,token))
                .andExpect(status().isNotFound());

        verify(userService,times(1)).deleteFriend(2);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetUserChatsSuccess() throws Exception{
        List<ChatRoomDTO> testList = Arrays.asList(
                new ChatRoomDTO(1,"testChat"),
                new ChatRoomDTO(2,"testChat")
        );
        PageRequest pageRequest = new PageRequest(0,2);
        Page<ChatRoomDTO> testPage = new PageImpl<>(testList,pageRequest,2);

        when(userService.getUserChats(pageRequest)).thenReturn(testPage);

        mockMvc.perform(get("/app/user/chats?page=0&size=2")
                .header(AUTHORIZATION,token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].id",is(1)))
                .andExpect(jsonPath("$.content[1].id",is(2)));

        verify(userService,times(1)).getUserChats(pageRequest);
        verifyNoMoreInteractions(userService);
    }


}
