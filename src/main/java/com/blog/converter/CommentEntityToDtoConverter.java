package com.blog.converter;

import com.blog.entities.Comment;
import com.blog.exceptions.BlogNotFoundException;
import com.blog.exceptions.UserNotFoundException;
import com.blog.models.CommentDto;
import com.blog.repositories.BlogRepository;
import com.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This class is responsible for converting comment entity to comment dto and vice versa.
 */
@Component
@RequiredArgsConstructor
public class CommentEntityToDtoConverter {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    /**
     * This method is used to convert comment dto to comment entity.
     * @param commentDto to convert comment entity.
     * @return converted comment entity.
     * @throws UserNotFoundException if user not found.
     * @throws BlogNotFoundException if blog not found.
     */
    public Comment convertCommentDtoToEntity(CommentDto commentDto) {
        return Comment.builder()
                .commentId(commentDto.getCommentId())
                .user(
                        userRepository.findById(UUID.fromString(commentDto.getUser())).orElseThrow(
                                () -> new UserNotFoundException("User not found with id : " + commentDto.getUser())
                        )
                )
                .blog(
                        blogRepository.findById(UUID.fromString(commentDto.getBlog())).orElseThrow(
                                () -> new BlogNotFoundException("Blog not found with blog id : " + commentDto.getBlog())
                        )
                )
                .content(commentDto.getContent())
                .createdAt(commentDto.getCreatedAt())
                .build();
    }

    /**
     * This method is used to convert comment entity to comment dto.
     * @param comment to convert comment dto.
     * @return converted comment dto.
     */
    public CommentDto convertCommentEntityToDto(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .blog(comment.getBlog().getBlogId().toString())
                .user(comment.getUser().getUserId().toString())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
