package com.example.authservice.controller;

import com.example.authservice.entity.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class AuthController {

    private final UserDetailsManager userDetailsManager;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> registerUser(@RequestBody RegisterUserDto request) {
        String email = request.email();
        String password = request.password();
        UserDetails user = User.builder()
                .username(email)
                .password(password)
                .build();
        userDetailsManager.createUser(user);
        return ResponseEntity.ok(new RegisterUserResponseDto("Created user: " + email));
    }
}
