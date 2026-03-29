package com.example.authservice.controller.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenGenerator {

    public String generateToken(String email, String password) {
        return "jes";
    }
}
