package com.blog.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This model class is used to read and write comments data to user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private UUID commentId;
    private String blog;
    private String user;
    private String content;
    private LocalDateTime createdAt;
}
