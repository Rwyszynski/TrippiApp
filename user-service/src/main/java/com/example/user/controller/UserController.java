package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.entity.dto.ResponseUsersDto;
import com.example.user.entity.dto.UserDto;
import com.example.user.mapper.UserMapper;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<UserDto> getUserById(Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseUsersDto> searchUsers(String query) {
        List<User> user = userService.searchUsers(query);
        return ResponseEntity.ok(new ResponseUsersDto(userMapper.toResponseUsersDto(user)));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        // Implementation to retrieve the current authenticated user
        return ResponseEntity.ok(new UserDto("current_user", " "));
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserDto> updateUserProfile(UserDto userDto) {
        User updatedUser = userService.updateUserProfile(userDto);
        return ResponseEntity.ok(userMapper.toUserDto(updatedUser));
    }

}
