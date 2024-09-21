package com.blog.converter;

import com.blog.entities.Blog;
import com.blog.entities.Comment;
import com.blog.entities.User;
import com.blog.models.BlogDto;
import com.blog.models.CommentDto;
import com.blog.models.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserEntityToDtoConverterTest {

    @InjectMocks
    private UserEntityToDtoConverter converter;
    @Mock
    private BlogEntityToDtoConverter blogEntityToDtoConverter;
    private UserDto userDto;
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
        String content = "My first blog content";
        String username = "Sreekanth";
        String title = "MyBlog";
        user = User.builder()
                .userId(userId)
                .username(username)
                .passwordHash(passwordHash)
                .createdAt(createdAt)
                .email(email)
                .build();
        userDto = UserDto.builder()
                .createdAt(createdAt)
                .passwordHash(passwordHash)
                .username(username)
                .email(email)
                .build();
        commentDto = new CommentDto(commentId, blogId.toString(), userId.toString(), content, createdAt);
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
    void testConvertUserDtoToEntity_WithValidData_ShouldConvertSuccessfully() {
        // given
        userDto.setBlogs(Set.of(blogDto));
        // when
        when(blogEntityToDtoConverter.convertBlogDtoToEntity(blogDto)).thenReturn(blog);
        // then
        var actual = converter.convertUserDtoToEntity(userDto);
        assertEquals(userDto.getUserId(), actual.getUserId());
        assertEquals(userDto.getUsername(), actual.getUsername());
        assertEquals(userDto.getEmail(), actual.getEmail());
        assertEquals(userDto.getCreatedAt(), actual.getCreatedAt());
        assertEquals(userDto.getPasswordHash(), actual.getPasswordHash());
        assertEquals(userDto.getBlogs().size(), actual.getBlogs().size());
        // verify
        verify(blogEntityToDtoConverter, times(1)).convertBlogDtoToEntity(blogDto);
    }

    @Test
    void testConvertUserEntityToDto_WithValidData_ShouldConvertSuccessfully() {
        var actual = converter.convertUserEntityToDto(user);
        assertEquals(user.getUserId(), actual.getUserId());
        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getCreatedAt(), actual.getCreatedAt());
        assertEquals(user.getPasswordHash(), actual.getPasswordHash());
    }
}