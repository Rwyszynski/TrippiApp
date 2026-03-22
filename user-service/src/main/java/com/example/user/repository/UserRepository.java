package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    List<User> findByUsernameContainingIgnoreCase(String query);

    Optional<User> findByUserName(String userName);
}
