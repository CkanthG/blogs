package com.blog.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * This model class is used to read and write users data to user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID userId;
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
    private Set<BlogDto> blogs;
}
