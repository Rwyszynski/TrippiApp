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

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.time.*;

@RequiredArgsConstructor
@Component
public class JwtTokenGenerator {

    private final AuthenticationManager authenticationManager;
    private final Clock clock;
    private final JwtConfigurationProperties properties;
    private final KeyPair keyPair;

    public String generateToken(String email, String password) throws NoSuchAlgorithmException {
        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication =  authenticationManager.authenticate(authenticate); //rodzaj autentykacji
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Instant now = LocalDateTime.now(clock).toInstant(ZoneOffset.UTC);
        Instant addedTime = now.plus(Duration.ofMinutes(properties.expirationMinutes()));  //10 minut
        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) keyPair.getPrivate());
        return JWT.create()                 //tworzenie tokena
                .withSubject(String.valueOf(securityUser.getId()))
                .withIssuedAt(now)
                .withExpiresAt(addedTime)
                .withIssuer(properties.issuer())
                .withKeyId("key-1")
                .withClaim("roles", securityUser.getAuthoritesAsString())
                .withClaim("preferred_username", securityUser.getUsername())
                .sign(algorithm);
    }
}
