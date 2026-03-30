package com.example.authservice.controller.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.authservice.config.JwtConfigurationProperties;
import com.example.authservice.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.time.*;

@RequiredArgsConstructor
@Component
public class JwtTokenGenerator {

    private final AuthenticationManager authenticationManager;
    private final Clock clock;
    private final JwtConfigurationProperties properties;

    public String generateToken(String email, String password) {
        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication =  authenticationManager.authenticate(authenticate); //rodzaj autentykacji
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Instant now = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant addedTime = now.plus(Duration.ofMinutes(properties.expirationMinutes()));  //10 minut
        return JWT.create()                 //tworzenie tokena
                .withSubject(securityUser.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(addedTime)
                .withIssuer(properties.issuer())
                .withClaim("roles", securityUser.getAuthoritesAsString())
                .sign(Algorithm.HMAC256(properties.secret()));
    }
}
