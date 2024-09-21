package com.blog.converter;

import com.blog.entities.Blog;
import com.blog.entities.Comment;
import com.blog.entities.User;
import com.blog.exceptions.BlogNotFoundException;
import com.blog.models.BlogDto;
import com.blog.models.CommentDto;
import com.blog.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlogEntityToDtoConverterTest {

    @InjectMocks
    private BlogEntityToDtoConverter converter;
    @Mock
    private UserRepository repository;
    @Mock
    private CommentEntityToDtoConverter commentEntityToDtoConverter;
    private User user;
    private Blog blog;
    private Comment comment;
    private CommentDto commentDto;
    private final UUID userId = UUID.randomUUID();
    private final LocalDateTime createdAt = LocalDateTime.now();
    private BlogDto blogDto;
    private final UUID blogId = UUID.randomUUID();
    private final UUID commentId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // given
        String email = "sree@gmail.com";
        String passwordHash = "39fc2d7152bf2b2bbed41b8e942894f5505896107aa60dd4a069f907e671b011";
        String username = "Sreekanth";
        user = User.builder()
                .userId(userId)
                .username(username)
                .passwordHash(passwordHash)
                .createdAt(createdAt)
                .email(email)
                .build();
        String content = "My first blog content";
        String blogName = "MyBlog";
        commentDto = new CommentDto(commentId, blogName, userId.toString(), content, createdAt);
        String title = "MyBlog";
        blogDto = BlogDto.builder()
                .blogId(blogId)
                .title(title)
                .user(userId.toString())
                .createdAt(createdAt)
                .comments(Set.of(commentDto))
                .build();
        comment = Comment.builder()
                .commentId(commentId)
                .build();
        blog = Blog.builder()
                .blogId(blogId)
                .user(user)
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .comments(Set.of(comment))
                .build();
    }

    @Test
    void testConvertBlogDtoToEntity_withValidData_shouldConvertSuccessfully() {
        // when
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(commentEntityToDtoConverter.convertCommentDtoToEntity(commentDto)).thenReturn(comment);
        // then
        var actual = converter.convertBlogDtoToEntity(blogDto);
        assertEquals(blogDto.getUser(), actual.getUser().getUserId().toString());
        assertEquals(blogDto.getBlogId(), actual.getBlogId());
        assertEquals(blogDto.getContent(), actual.getContent());
        assertEquals(blogDto.getCreatedAt(), actual.getCreatedAt());
        assertEquals(blogDto.getTitle(), actual.getTitle());
        // verify
        verify(repository, times(1)).findById(userId);
        verify(commentEntityToDtoConverter, times(1)).convertCommentDtoToEntity(commentDto);
    }

    @Test
    void testConvertBlogDtoToEntity_withInValidData_shouldThrowBlogNotFoundException() {
        assertThrows(
                BlogNotFoundException.class,
                () -> converter.convertBlogDtoToEntity(blogDto)
        );
    }

    @Test
    void testConvertBlogEntityToDto_withValidData_shouldConvertSuccessfully() {
        var actual = converter.convertBlogEntityToDto(blog);
        assertEquals(blog.getBlogId(), actual.getBlogId());
        assertEquals(blog.getContent(), actual.getContent());
        assertEquals(blog.getCreatedAt(), actual.getCreatedAt());
        assertEquals(blog.getTitle(), actual.getTitle());
        assertEquals(blog.getUser().getUserId().toString(), actual.getUser());
    }
}