package com.blog.converter;

import com.blog.entities.User;
import com.blog.models.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserEntityToDtoConverter {

    private final BlogEntityToDtoConverter blogEntityToDtoConverter;

    /**
     * This method is used to convert user dto to user entity.
     * @param userDto to convert user entity.
     * @return converted user entity.
     */
    public User convertUserDtoToEntity(UserDto userDto) {
        return User.builder()
                .userId(userDto.getUserId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .passwordHash(userDto.getPasswordHash())
                .createdAt(userDto.getCreatedAt())
                .blogs(
                        userDto.getBlogs() != null ? userDto.getBlogs().stream().map(
                                blogEntityToDtoConverter::convertBlogDtoToEntity
                        ).collect(Collectors.toSet()) : null
                )
                .build();
    }

    /**
     * This method is used to convert user entity to user dto.
     * @param user to convert user dto.
     * @return converted user dto.
     */
    public UserDto convertUserEntityToDto(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
