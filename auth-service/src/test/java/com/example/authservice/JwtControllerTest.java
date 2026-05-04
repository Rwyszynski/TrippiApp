package com.example.authservice;

import com.example.authservice.controller.jwt.JwtController;
import com.example.authservice.controller.jwt.JwtTokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JwtControllerTest {

    @Mock
    private JwtTokenGenerator jwtTokenGenerator;

    private MockMvc mockMvc;
    private KeyPair keyPair;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
        JwtController jwtController = new JwtController(jwtTokenGenerator, keyPair);
        mockMvc = MockMvcBuilders.standaloneSetup(jwtController).build();
    }

    @Test
    void shouldGenerateToken() throws Exception {
        when(jwtTokenGenerator.generateToken("test@test.com", "password")).thenReturn("mock.jwt.token");

        mockMvc.perform(post("/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"test@test.com","password":"password"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock.jwt.token"));
    }

    @Test
    void shouldReturnJwks() throws Exception {
        mockMvc.perform(get("/v1/auth/.well-known/jwks.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keys").isArray())
                .andExpect(jsonPath("$.keys[0].kty").value("RSA"))
                .andExpect(jsonPath("$.keys[0].alg").value("RS256"))
                .andExpect(jsonPath("$.keys[0].kid").value("key-1"));
    }

    @Test
    void shouldThrowWhenInvalidCredentials() throws Exception {
        when(jwtTokenGenerator.generateToken("wrong@test.com", "wrong"))
                .thenThrow(new RuntimeException("Bad credentials"));

        assertThatThrownBy(() ->
                mockMvc.perform(post("/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"wrong@test.com","password":"wrong"}
                                """))
        ).hasCauseInstanceOf(RuntimeException.class);
    }
}
