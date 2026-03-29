package com.example.authservice.controller.jwt;

import lombok.Builder;

@Builder
public record JwtResponseDto(String token) {}
