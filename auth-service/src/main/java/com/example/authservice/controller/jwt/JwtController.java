package com.example.authservice.controller.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@RestController
public class JwtController {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final KeyPair keyPair;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> generateToken(@RequestBody TokenRequestDto token) throws NoSuchAlgorithmException {
        String genToken = jwtTokenGenerator.generateToken(token.email(), token.password());
        return ResponseEntity.ok(JwtResponseDto.builder()
                .token(genToken)
                .build()
        );
    }
    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getJwks() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        return Map.of(
                "keys", List.of(
                        Map.of(
                                "kty", "RSA",
                                "alg", "RS256",
                                "use", "sig",
                                "kid", "key-1",
                                "n", Base64.getUrlEncoder().withoutPadding()
                                        .encodeToString(publicKey.getModulus().toByteArray()),
                                "e", Base64.getUrlEncoder().withoutPadding()
                                        .encodeToString(publicKey.getPublicExponent().toByteArray())
                        )
                )
        );
    }

}
