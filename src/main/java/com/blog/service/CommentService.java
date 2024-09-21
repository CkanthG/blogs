package com.blog.service;

import com.blog.converter.CommentEntityToDtoConverter;
import com.blog.entities.Blog;
import com.blog.entities.Comment;
import com.blog.models.CommentDto;
import com.blog.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class is used to handle all business logic of comments.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentEntityToDtoConverter commentEntityToDtoConverter;

    /**
     * This method is used to create a comment.
     * @param commentDto is used to create a comment.
     * @return created comment.
     */
    public CommentDto createComment(CommentDto commentDto) {
        Comment comment = commentRepository.save(commentEntityToDtoConverter.convertCommentDtoToEntity(commentDto));
        return commentEntityToDtoConverter.convertCommentEntityToDto(comment);
    }

    /**
     * This method is used to find all comments by specific blog.
     * @param blog to identify all comments.
     * @return list of blog comments.
     */
    public List<CommentDto> findCommentsByBlog(Blog blog) {
        return commentRepository.findByBlog(blog).stream().map(
                commentEntityToDtoConverter::convertCommentEntityToDto
        ).toList();
    }
}

