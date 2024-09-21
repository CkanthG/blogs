package com.blog.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * This model class is used to read and write blogs data to user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto {
    private UUID blogId;
    private String user;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Set<CommentDto> comments;
}
