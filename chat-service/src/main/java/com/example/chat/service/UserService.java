package com.example.chat.service;

import com.example.chat.config.UserClient;
import com.example.chat.entity.dto.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public UserResponseDto getUser(Long id) {
        return userClient.getUserById(id);
    }
}
