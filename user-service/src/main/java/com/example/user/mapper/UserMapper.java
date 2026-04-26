package com.example.user.mapper;

import com.example.user.entity.User;
import com.example.user.entity.dto.UserDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserMapper {

    public List<UserDto> toResponseUsersDto(List<User> users) {
        return users.stream()
                .map(this::toUserDto)
                .toList();
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUserName(), user.getAvatarUrl(), user.getCountry(), user.getGender(), user.getAge());
    }
}
