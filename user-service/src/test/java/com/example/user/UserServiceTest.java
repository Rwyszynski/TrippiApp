package com.example.user;

import com.example.user.entity.User;
import com.example.user.entity.dto.CreateUserRequest;
import com.example.user.entity.dto.UserDto;
import com.example.user.exception.UserAlreadyExistsException;
import com.example.user.repository.UserRepository;
import com.example.user.service.ChatService;
import com.example.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldGetAllUsers() {
        List<User> users = List.of(new User("user1@test.com"), new User("user2@test.com"));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserName()).isEqualTo("user1@test.com");
    }

    @Test
    void shouldGetUserById() {
        User user = new User("test@test.com");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertThat(result.getUserName()).isEqualTo("test@test.com");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldSearchUsers() {
        List<User> users = List.of(new User("john@test.com"));
        when(userRepository.findByUserNameContainingIgnoreCase("john")).thenReturn(users);

        List<User> result = userService.searchUsers("john");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("john@test.com");
    }

    @Test
    void shouldReturnEmptyListWhenSearchFindsNothing() {
        when(userRepository.findByUserNameContainingIgnoreCase("xyz")).thenReturn(List.of());

        List<User> result = userService.searchUsers("xyz");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldUpdateUserProfile() {
        User user = new User("test@test.com");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "1")
                .build();

        UserDto userDto = new UserDto(null, null, null, "Poland", "Male", 25);
        User result = userService.updateUserProfile(userDto, jwt);

        assertThat(result.getCountry()).isEqualTo("Poland");
        assertThat(result.getGender()).isEqualTo("Male");
        assertThat(result.getAge()).isEqualTo(25);
        verify(userRepository).save(user);
    }

    @Test
    void shouldCreateUser() {
        when(userRepository.findByUserName("new@test.com")).thenReturn(Optional.empty());

        userService.createUser(new CreateUserRequest("new@test.com"));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenUserAlreadyExists() {
        User existing = new User("existing@test.com");
        when(userRepository.findByUserName("existing@test.com")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> userService.createUser(new CreateUserRequest("existing@test.com")))
                .isInstanceOf(UserAlreadyExistsException.class);
    }
}