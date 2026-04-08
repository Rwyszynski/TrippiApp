package com.example.user.service;

import com.example.user.entity.dto.CreateUserRequest;
import com.example.user.entity.dto.UserNameDto;
import com.example.user.entity.User;
import com.example.user.entity.dto.UserDto;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChatService chatService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> searchUsers(String query) {
        List<User> users = userRepository.findByUserNameContainingIgnoreCase(query);
        return users.isEmpty() ? List.of() : users;
    }

    public User updateUserProfile(UserDto userDto) {
        User user = userRepository.findByUserName(userDto.userName()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserName(userDto.userName());
        user.setAvatarUrl(userDto.avatarUrl());
        return userRepository.save(user);
    }

    public UserNameDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return new UserNameDto(username);
    }

    public void createUser(CreateUserRequest request) {
        User user = new User();
        user.setUserName(request.email());

        userRepository.save(user);
    }
}
