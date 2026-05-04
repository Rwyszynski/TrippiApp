package com.example.user;

import com.example.user.controller.UserController;
import com.example.user.entity.User;
import com.example.user.entity.dto.*;
import com.example.user.mapper.UserMapper;
import com.example.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        List<User> users = List.of(new User("user1@test.com"));
        List<UserDto> userDtos = List.of(new UserDto(null, "user1@test.com", null, null, null, null));
        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.toResponseUsersDto(users)).thenReturn(userDtos);

        mockMvc.perform(get("/v1/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].userName").value("user1@test.com"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        User user = new User("test@test.com");
        user.setId(1L);
        UserDto userDto = new UserDto(1L, "test@test.com", null, null, null, null);
        when(userService.getUserById(1L)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("test@test.com"));
    }

    @Test
    void shouldSearchUsers() throws Exception {
        List<User> users = List.of(new User("john@test.com"));
        List<UserDto> userDtos = List.of(new UserDto(null, "john@test.com", null, null, null, null));
        when(userService.searchUsers("john")).thenReturn(users);
        when(userMapper.toResponseUsersDto(users)).thenReturn(userDtos);

        mockMvc.perform(get("/v1/users/search").param("query", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].userName").value("john@test.com"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        doNothing().when(userService).createUser(any());

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"new@test.com"}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowWhenUserNotFound() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new RuntimeException("User not found"));

        assertThatThrownBy(() ->
                mockMvc.perform(get("/v1/users/99")))
                .hasCauseInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }
}