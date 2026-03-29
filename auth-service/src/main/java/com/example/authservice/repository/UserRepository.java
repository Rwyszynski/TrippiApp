package com.example.authservice.repository;

import com.example.authservice.entity.AuthUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AuthUser, Long> {

    AuthUser save(AuthUser authUser);

    Optional<AuthUser> findFirstByEmail(String username);
}
