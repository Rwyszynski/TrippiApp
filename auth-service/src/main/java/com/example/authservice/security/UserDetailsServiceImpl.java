package com.example.authservice.security;

import com.example.authservice.config.UserClient;
import com.example.authservice.entity.AuthUser;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.dto.CreateUserRequest;
import com.example.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.time.LocalDateTime;
import java.util.Set;

@Log4j2
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findFirstByEmail(username)
                .map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("Could't find user"));
    }

    @Override
    public void createUser(UserDetails user) {
        if (userExists(user.getUsername())) {
            log.warn("User already exists");
            throw new UsernameNotFoundException("Not saved - Username already exists");
        }

        AuthUser createdUser = new AuthUser(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                LocalDateTime.now(),
                Set.of(Role.STANDARD)
        );

        AuthUser newUser = userRepository.save(createdUser);
        userClient.createUser(new CreateUserRequest(newUser.getEmail()));

        log.info("Created user: " + user.getUsername());
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

}
