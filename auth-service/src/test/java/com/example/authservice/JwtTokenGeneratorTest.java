package com.example.authservice;

import com.example.authservice.config.JwtConfigurationProperties;
import com.example.authservice.controller.jwt.JwtTokenGenerator;
import com.example.authservice.security.SecurityUser;
import com.example.authservice.entity.AuthUser;
import com.example.authservice.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenGeneratorTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtConfigurationProperties properties;

    @InjectMocks
    private JwtTokenGenerator jwtTokenGenerator;

    private KeyPair keyPair;
    private Clock clock;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
        clock = Clock.fixed(Instant.now(), ZoneId.of("UTC"));

        jwtTokenGenerator = new JwtTokenGenerator(authenticationManager, clock, properties, keyPair);
    }

    @Test
    void shouldGenerateToken() throws NoSuchAlgorithmException {
        AuthUser authUser = new AuthUser("test@test.com", "password", LocalDateTime.now(), Set.of(Role.STANDARD));
        authUser.setId(1L);
        SecurityUser securityUser = new SecurityUser(authUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(properties.expirationMinutes()).thenReturn(10L);
        when(properties.issuer()).thenReturn("test-issuer");

        String token = jwtTokenGenerator.generateToken("test@test.com", "password");

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void shouldThrowWhenInvalidCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> jwtTokenGenerator.generateToken("wrong@test.com", "wrong"))
                .isInstanceOf(BadCredentialsException.class);
    }
}
