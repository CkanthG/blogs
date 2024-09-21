package com.blog.converter;

import com.blog.entities.Blog;
import com.blog.entities.Comment;
import com.blog.entities.User;
import com.blog.exceptions.BlogNotFoundException;
import com.blog.exceptions.UserNotFoundException;
import com.blog.models.BlogDto;
import com.blog.models.CommentDto;
import com.blog.repositories.BlogRepository;
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

class CommentEntityToDtoConverterTest {

    @InjectMocks
    private CommentEntityToDtoConverter converter;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BlogRepository blogRepository;
    private User user;
    private Blog blog;
    private Comment comment;
    private CommentDto commentDto;
    private final UUID userId = UUID.randomUUID();
    private final LocalDateTime createdAt = LocalDateTime.now();
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
        commentDto = new CommentDto(commentId, blogId.toString(), userId.toString(), content, createdAt);
        String title = "MyBlog";
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
    void testConvertCommentDtoToEntity_WithValidData_ShouldConvertSuccessfully() {
        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        // then
        var actual = converter.convertCommentDtoToEntity(commentDto);
        assertEquals(commentDto.getCommentId(), actual.getCommentId());
        assertEquals(commentDto.getBlog(), actual.getBlog().getBlogId().toString());
        assertEquals(commentDto.getUser(), actual.getUser().getUserId().toString());
        assertEquals(commentDto.getContent(), actual.getContent());
        assertEquals(commentDto.getCreatedAt(), actual.getCreatedAt());
        // verify
        verify(userRepository, times(1)).findById(userId);
        verify(blogRepository, times(1)).findById(blogId);
    }

    @Test
    void testConvertCommentDtoToEntity_WithInValidUserData_ShouldThrowUserNotFoundException() {
        assertThrows(
                UserNotFoundException.class,
                () -> converter.convertCommentDtoToEntity(commentDto)
        );
    }

    @Test
    void testConvertCommentDtoToEntity_WithInValidUserData_ShouldThrowBlogNotFoundException() {
        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // then
        assertThrows(
                BlogNotFoundException.class,
                () -> converter.convertCommentDtoToEntity(commentDto)
        );
        // verify
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testConvertCommentEntityToDto_WithValidData_ShouldConvertSuccessfully() {
        comment.setBlog(blog);
        comment.setUser(user);
        var actual = converter.convertCommentEntityToDto(comment);
        assertEquals(comment.getCommentId(), actual.getCommentId());
        assertEquals(comment.getBlog().getBlogId().toString(), actual.getBlog());
        assertEquals(comment.getUser().getUserId().toString(), actual.getUser());
        assertEquals(comment.getContent(), actual.getContent());
        assertEquals(comment.getCreatedAt(), actual.getCreatedAt());
    }
}