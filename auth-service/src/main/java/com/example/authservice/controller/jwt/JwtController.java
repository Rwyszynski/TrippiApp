package com.example.authservice.controller.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class JwtController {

    private final JwtTokenGenerator jwtTokenGenerator;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> generateToken(@RequestBody TokenRequestDto token){
        String genToken = jwtTokenGenerator.generateToken(token.email(), token.password());
        return ResponseEntity.ok(JwtResponseDto.builder()
                .token(genToken)
                .build()
        );
    }

}
