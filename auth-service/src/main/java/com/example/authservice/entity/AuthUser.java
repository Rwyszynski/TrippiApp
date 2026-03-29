package com.example.authservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "authUser")
@Entity
public class AuthUser {

    public AuthUser(String email, String password, LocalDateTime createdAt, Set<Role> role) {
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_user_seq")
    private Long id;

    private String email;

    private String password;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> role = new HashSet<>();

}
