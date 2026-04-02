package com.example.chat.config;

import com.example.chat.entity.dto.CreateUserRequest;
import com.example.chat.entity.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "user-service",
        url = "${services.user-service.url}",
        configuration = FeignConfig.class
)
public interface UserClient {

    @GetMapping("/v1/users/{id}")
    UserResponseDto getUserById(@PathVariable("id") Long id);
/*
    @PostMapping("/api/users")
    UserResponseDto createUser(@RequestBody CreateUserRequest request);
    */

}
