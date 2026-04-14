package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.entity.dto.CreateUserRequest;
import com.example.user.entity.dto.ResponseUsersDto;
import com.example.user.entity.dto.UserDto;
import com.example.user.entity.dto.UserNameDto;
import com.example.user.mapper.UserMapper;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/users")
@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/all")
    public ResponseEntity<ResponseUsersDto> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ResponseUsersDto(userMapper.toResponseUsersDto(users)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseUsersDto> searchUsers(@RequestParam String query) {
        List<User> user = userService.searchUsers(query);
        return ResponseEntity.ok(new ResponseUsersDto(userMapper.toResponseUsersDto(user)));
    }

    @GetMapping("/me")
    public ResponseEntity<UserNameDto> getCurrentUser() {
        UserNameDto user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserDto> updateUserProfile(UserDto userDto) {
        User updatedUser = userService.updateUserProfile(userDto);
        return ResponseEntity.ok(userMapper.toUserDto(updatedUser));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request);
        return ResponseEntity.ok().build();
    }
}
