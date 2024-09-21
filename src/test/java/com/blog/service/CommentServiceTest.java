package com.blog.service;

import com.blog.converter.CommentEntityToDtoConverter;
import com.blog.entities.Blog;
import com.blog.entities.Comment;
import com.blog.entities.User;
import com.blog.models.CommentDto;
import com.blog.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository repository;
    @Mock
    private CommentEntityToDtoConverter converter;
    private CommentDto commentDto;
    private Comment comment;
    private final UUID commentId = UUID.randomUUID();
    private final UUID blogId = UUID.randomUUID();
    private final UUID userId = UUID.randomUUID();
    private final LocalDateTime createdAt = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // given
        String content = "MyContent";
        commentDto = CommentDto.builder()
                .commentId(commentId)
                .blog(blogId.toString())
                .user(userId.toString())
                .content(content)
                .createdAt(createdAt)
                .build();
        comment = Comment.builder()
                .createdAt(createdAt)
                .content(content)
                .commentId(commentId)
                .user(
                        User.builder().userId(userId).build()
                )
                .blog(
                        Blog.builder().blogId(blogId).build()
                )
                .build();
    }

    @Test
    void testCreateComment_WithValidData_ShouldCreateComment() {
        // when
        when(converter.convertCommentDtoToEntity(commentDto)).thenReturn(comment);
        when(repository.save(comment)).thenReturn(comment);
        when(converter.convertCommentEntityToDto(comment)).thenReturn(commentDto);
        // then
        var actual = commentService.createComment(commentDto);
        assertEquals(commentDto.getCommentId(), actual.getCommentId());
        assertEquals(commentDto.getCreatedAt(), actual.getCreatedAt());
        assertEquals(commentDto.getContent(), actual.getContent());
        assertEquals(commentDto.getUser(), actual.getUser());
        assertEquals(commentDto.getBlog(), actual.getBlog());
        // verify
        verify(converter, times(1)).convertCommentDtoToEntity(commentDto);
        verify(repository, times(1)).save(comment);
        verify(converter, times(1)).convertCommentEntityToDto(comment);
    }

    @Test
    void testFindCommentsByBlog_WithValidData_ShouldReturnComments() {
        // given
        var blog = Blog.builder().blogId(blogId).build();
        // when
        when(repository.findByBlog(blog)).thenReturn(List.of(comment));
        when(converter.convertCommentEntityToDto(comment)).thenReturn(commentDto);
        // then
        var actual = commentService.findCommentsByBlog(blog);
        assert !actual.isEmpty();
        var response = actual.get(0);
        assertEquals(blog.getBlogId().toString(), response.getBlog());
        assertEquals(commentDto.getCommentId(), response.getCommentId());
        assertEquals(commentDto.getUser(), response.getUser());
        assertEquals(commentDto.getContent(), response.getContent());
        assertEquals(commentDto.getCreatedAt(), response.getCreatedAt());
        // verify
        verify(repository, times(1)).findByBlog(blog);
        verify(converter, times(1)).convertCommentEntityToDto(comment);
    }
}