package com.example.authservice.config;

import com.example.authservice.entity.dto.CreateUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
        name = "user-service",
        url = "http://localhost:8082",
        configuration = FeignConfig.class
)
public interface UserClient {

    @PostMapping("/v1/users")
    void createUser(@RequestBody CreateUserRequest request);
}
