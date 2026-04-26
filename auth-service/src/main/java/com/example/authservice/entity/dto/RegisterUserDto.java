package com.example.authservice.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(
        @Email(message = "Niepoprawny email")
        @NotBlank
        String email,

        @Size(min = 5, message = "Hasło musi mieć co najmniej 5 znaków")
        @NotBlank
        String password
) {}