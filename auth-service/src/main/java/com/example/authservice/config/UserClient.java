package com.example.authservice.config;

import com.example.authservice.entity.dto.CreateUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-service",
        url = "${services.user-service.url}"
)
public interface UserClient {

    @PostMapping("/v1/users")
    void createUser(@RequestBody CreateUserRequest request);
}
