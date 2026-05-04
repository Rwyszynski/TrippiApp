package com.example.authservice;

import com.example.authservice.security.UserDetailsServiceImpl;
import com.example.authservice.config.UserClient;
import com.example.authservice.entity.AuthUser;
import com.example.authservice.entity.Role;
import com.example.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldLoadUserByUsername() {
        AuthUser authUser = new AuthUser("test@test.com", "encoded", LocalDateTime.now(), Set.of(Role.STANDARD));
        when(userRepository.findFirstByEmail("test@test.com")).thenReturn(Optional.of(authUser));

        UserDetails result = userDetailsService.loadUserByUsername("test@test.com");

        assertThat(result.getUsername()).isEqualTo("test@test.com");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findFirstByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("notfound@test.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void shouldCreateUser() {
        when(userRepository.findFirstByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        AuthUser saved = new AuthUser("new@test.com", "encoded", LocalDateTime.now(), Set.of(Role.STANDARD));
        saved.setId(1L);
        when(userRepository.save(any())).thenReturn(saved);

        UserDetails user = User.builder().username("new@test.com").password("password").build();
        userDetailsService.createUser(user);

        verify(userRepository).save(any());
        verify(userClient).createUser(any());
    }

    @Test
    void shouldThrowWhenUserAlreadyExists() {
        AuthUser existing = new AuthUser("existing@test.com", "encoded", LocalDateTime.now(), Set.of(Role.STANDARD));
        when(userRepository.findFirstByEmail("existing@test.com")).thenReturn(Optional.of(existing));

        UserDetails user = User.builder().username("existing@test.com").password("password").build();

        assertThatThrownBy(() -> userDetailsService.createUser(user))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldReturnTrueWhenUserExists() {
        AuthUser authUser = new AuthUser("test@test.com", "encoded", LocalDateTime.now(), Set.of(Role.STANDARD));
        when(userRepository.findFirstByEmail("test@test.com")).thenReturn(Optional.of(authUser));

        assertThat(userDetailsService.userExists("test@test.com")).isTrue();
    }

    @Test
    void shouldReturnFalseWhenUserNotExists() {
        when(userRepository.findFirstByEmail(anyString())).thenReturn(Optional.empty());

        assertThat(userDetailsService.userExists("notfound@test.com")).isFalse();
    }
}
