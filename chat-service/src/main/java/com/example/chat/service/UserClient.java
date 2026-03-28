package com.example.chat.service;

import com.example.chat.entity.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class UserClient {

    private final RestClient restClient;

    public UserDto getUserById(Long id) {
        return restClient.get()
                .uri("/v1/users/{id}", id)
                .retrieve()
                .body(UserDto.class);
    }
}
