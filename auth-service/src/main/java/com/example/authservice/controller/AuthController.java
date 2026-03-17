package com.example.authservice.controller;

import com.example.authservice.entity.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/auth")
@RestController
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> registerUser(@RequestBody RegisterUserDto request) {
        //save
        return ResponseEntity.ok(new RegisterUserResponseDto("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDto> login(@RequestBody LoginUserDto request) {
        //authenticate and generate JWT token
        return ResponseEntity.ok(new LoginUserResponseDto("dummy-jwt-token"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        //validate refresh token and generate new JWT token
        return ResponseEntity.ok(new RefreshTokenResponseDto("new-dummy-jwt-token"));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestBody LogoutRequestDto request) {
        //invalidate refresh token
        return ResponseEntity.ok(new LogoutResponseDto("User logged out successfully"));
    }
}
