package com.study.yaroslavambrozyak.messenger.controllerTest;

import com.study.yaroslavambrozyak.messenger.TestUtil;
import com.study.yaroslavambrozyak.messenger.controller.ChatRoomController;
import com.study.yaroslavambrozyak.messenger.controller.ExceptionController;
import com.study.yaroslavambrozyak.messenger.dto.ChatRoomCreateDTO;
import com.study.yaroslavambrozyak.messenger.dto.ChatRoomDTO;
import com.study.yaroslavambrozyak.messenger.dto.MessageDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.exception.ChatRoomNotFoundException;
import com.study.yaroslavambrozyak.messenger.service.ChatRoomService;
import com.study.yaroslavambrozyak.messenger.service.TokenAuthenticationService;
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
public class ChatRoomControllerTest {

    @Autowired
    private ExceptionController exceptionController;
    @InjectMocks
    private ChatRoomController chatRoomController;
    @Mock
    private ChatRoomService chatRoomService;
    private MockMvc mockMvc;
    private String token;
    private final String AUTHORIZATION = "Authorization";

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        token = TokenAuthenticationService.createToken("testUser");
        mockMvc = MockMvcBuilders.standaloneSetup(chatRoomController)
                .setControllerAdvice(exceptionController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void testGetChatRoomSuccess() throws Exception{
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO(1,"testChat");
        when(chatRoomController.getChatRoom(1)).thenReturn(chatRoomDTO);

        mockMvc.perform(get("/app/chat/1")
                .header(AUTHORIZATION,token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.name",is("testChat")));

        verify(chatRoomService,times(1)).getChatRoom(1);
        verifyNoMoreInteractions(chatRoomService);
    }

    @Test
    public void testGetChatRoomNotFound() throws Exception{
        when(chatRoomController.getChatRoom(1)).thenThrow(new ChatRoomNotFoundException());

        mockMvc.perform(get("/app/chat/1")
                .header(AUTHORIZATION,token))
                .andExpect(status().isNotFound());

        verify(chatRoomService,times(1)).getChatRoom(1);
        verifyNoMoreInteractions(chatRoomService);
    }

    @Test
    public void testGetChatRoomMessagesSuccess() throws Exception{
        List<MessageDTO> testList = Arrays.asList(
                new MessageDTO("text1",1),
                new MessageDTO("text2",2)
        );
        PageRequest pageRequest = new PageRequest(0,2);
        Page<MessageDTO> page = new PageImpl<>(testList,pageRequest,2);

        when(chatRoomController.getChatMessages(1,pageRequest)).thenReturn(page);

        mockMvc.perform(get("/app/chat/1/messages?page=0&size=2")
                .header(AUTHORIZATION,token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].text",is("text1")))
                .andExpect(jsonPath("$.content[1].text",is("text2")));

        verify(chatRoomService,times(1)).getChatMessages(1,pageRequest);
        verifyNoMoreInteractions(chatRoomService);
    }

    @Test
    public void testGetUsersInChatRoomSuccess() throws Exception{
        List<UserDTO> testList = Arrays.asList(
                new UserDTO(1,"testName","testSurName"),
                new UserDTO(2,"testName","testSurName")
        );
        PageRequest pageRequest = new PageRequest(0,2);
        Page<UserDTO> page = new PageImpl<>(testList,pageRequest,2);

        when(chatRoomController.getUsersInChat(1,pageRequest)).thenReturn(page);

        mockMvc.perform(get("/app/chat/1/users?page=0&size=2")
                .header(AUTHORIZATION,token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].id",is(1)))
                .andExpect(jsonPath("$.content[1].id",is(2)));

        verify(chatRoomService,times(1)).getUsersInChat(1,pageRequest);
        verifyNoMoreInteractions(chatRoomService);
    }
    //TODO fix 400 resp
    @Test
    public void testCreateChatRoomSuccess() throws Exception{
        ChatRoomCreateDTO createDTO = new ChatRoomCreateDTO("testRoom");
        when(chatRoomService.createChatRoom(createDTO)).thenReturn(1L);

        mockMvc.perform(post("/app/chat")
                .header(AUTHORIZATION,token)
                .content(TestUtil.asJsonString(createDTO)))
                .andExpect(status().isCreated());

        verify(chatRoomService,times(1)).createChatRoom(refEq(createDTO));
        verifyNoMoreInteractions(chatRoomService);
    }

    @Test
    public void testAddUserToChat() throws Exception{
        doNothing().when(chatRoomService).addUserToChat(1,2);

        mockMvc.perform(post("/app/chat/1/add/2")
                .header(AUTHORIZATION,token))
                .andExpect(status().isOk());

        verify(chatRoomService,times(1)).addUserToChat(1,2);
        verifyNoMoreInteractions(chatRoomService);
    }

    @Test
    public void testAddUserToChatNotFound() throws Exception{
        doThrow(new ChatRoomNotFoundException()).when(chatRoomService).addUserToChat(1,2);

        mockMvc.perform(post("/app/chat/1/add/2")
                .header(AUTHORIZATION,token))
                .andExpect(status().isNotFound());

        verify(chatRoomService,times(1)).addUserToChat(1,2);
        verifyNoMoreInteractions(chatRoomService);
    }

    @Test
    public void testDeleteUserFromChatSuccess() throws Exception{
        doNothing().when(chatRoomService).deleteUserFromChat(1,1);

        mockMvc.perform(delete("/app/chat/1/delete/1")
                .header(AUTHORIZATION,token))
                .andExpect(status().isOk());

        verify(chatRoomService,times(1)).deleteUserFromChat(1,1);
        verifyNoMoreInteractions(chatRoomService);
    }

    @Test
    public void testDeleteUserFromChatNotFound() throws Exception{
        doThrow(new ChatRoomNotFoundException()).when(chatRoomService).deleteUserFromChat(1,1);

        mockMvc.perform(delete("/app/chat/1/delete/1")
                .header(AUTHORIZATION,token))
                .andExpect(status().isNotFound());

        verify(chatRoomService,times(1)).deleteUserFromChat(1,1);
        verifyNoMoreInteractions(chatRoomService);
    }
}
