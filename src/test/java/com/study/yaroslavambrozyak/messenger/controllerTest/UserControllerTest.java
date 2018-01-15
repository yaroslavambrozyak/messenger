package com.study.yaroslavambrozyak.messenger.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.yaroslavambrozyak.messenger.controller.ExceptionController;
import com.study.yaroslavambrozyak.messenger.controller.UserController;
import com.study.yaroslavambrozyak.messenger.dto.UserDTO;
import com.study.yaroslavambrozyak.messenger.dto.UserUpdateDTO;
import com.study.yaroslavambrozyak.messenger.entity.User;
import com.study.yaroslavambrozyak.messenger.exception.UserNotFoundException;
import com.study.yaroslavambrozyak.messenger.service.TokenAuthenticationService;
import com.study.yaroslavambrozyak.messenger.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        token = TokenAuthenticationService.createToken("testUser");
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(exceptionController)
                .build();

    }

    @Test
    public void testGetUserByIdSuccess() throws Exception {

        UserDTO testUserDTO = new UserDTO();
        testUserDTO.setId(1);
        testUserDTO.setName("testName");
        testUserDTO.setSurName("testSurName");

        when(userController.getUser(1)).thenReturn(testUserDTO);

        mockMvc.perform(get("/app/user/1").header("Authorization",token))
                .andExpect(status().isOk());

        verify(userService,times(1)).getUserById(1);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        when(userController.getUser(1)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/app/user/1").header("Authorization",token))
                .andExpect(status().isNotFound());

        verify(userService,times(1)).getUserById(1);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testCreateUserSuccess() throws Exception {
        UserUpdateDTO testUserUpdateDTO = new UserUpdateDTO();
        doNothing().when(userService).updateUser(testUserUpdateDTO);

        mockMvc.perform(put("/app/user")
                .header("Authorization",token)
                .content(asJsonString(testUserUpdateDTO)))
                .andExpect(status().isOk());

       verify(userService,times(1)).updateUser(refEq(testUserUpdateDTO));
       verifyNoMoreInteractions(userService);
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        doNothing().when(userService).deleteUser();

        mockMvc.perform(delete("/app/user").header("Authorization",token))
                .andExpect(status().isOk());

        verify(userService,times(1)).deleteUser();
        verifyNoMoreInteractions(userService);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
