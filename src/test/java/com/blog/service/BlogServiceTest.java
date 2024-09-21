package com.blog.service;

import com.blog.converter.BlogEntityToDtoConverter;
import com.blog.entities.Blog;
import com.blog.entities.Comment;
import com.blog.entities.User;
import com.blog.exceptions.BlogNotFoundException;
import com.blog.models.BlogDto;
import com.blog.models.CommentDto;
import com.blog.repositories.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlogServiceTest {

    @InjectMocks
    private BlogService blogService;
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private BlogEntityToDtoConverter converter;
    private User user;
    private Blog blog;
    private BlogDto blogDto;
    private final UUID blogId = UUID.randomUUID();
    private final String userId = UUID.randomUUID().toString();
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final UUID commentId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // given
        String content = "My first blog content";
        String blogName = "MyBlog";
        CommentDto commentDto = new CommentDto(commentId, blogName, userId, content, createdAt);
        String title = "MyBlog";
        blogDto = BlogDto.builder()
                .blogId(blogId)
                .title(title)
                .user(userId)
                .createdAt(createdAt)
                .comments(Set.of(commentDto))
                .build();
        user = User.builder()
                .userId(UUID.fromString(userId))
                .build();
        Comment comment = Comment.builder()
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
    void testCreateBlog_WithValidData_ShouldCreateBlog() {
        // when
        when(converter.convertBlogDtoToEntity(blogDto)).thenReturn(blog);
        when(blogRepository.save(blog)).thenReturn(blog);
        when(converter.convertBlogEntityToDto(blog)).thenReturn(blogDto);
        // then
        var actual = blogService.createBlog(blogDto);
        assertEquals(blogDto.getTitle(), actual.getTitle());
        assertEquals(blogDto.getUser(), actual.getUser());
        assertEquals(blogDto.getContent(), actual.getContent());
        assertEquals(blogDto.getCreatedAt(), actual.getCreatedAt());
        // verify
        verify(converter, times(1)).convertBlogDtoToEntity(blogDto);
        verify(blogRepository, times(1)).save(blog);
        verify(converter, times(1)).convertBlogEntityToDto(blog);
    }

    @Test
    void testFindBlogsByUser_WithValidData_ShouldReturnBlog() {
        // when
        when(blogRepository.findByUser(user)).thenReturn(List.of(blog));
        when(converter.convertBlogEntityToDto(blog)).thenReturn(blogDto);
        // then
        List<BlogDto> actual = blogService.findBlogsByUser(user);
        assert !actual.isEmpty();
        var blogResponse = actual.get(0);
        assertEquals(blogDto.getBlogId(), blogResponse.getBlogId());
        assertEquals(blogDto.getTitle(), blogResponse.getTitle());
        assertEquals(blogDto.getUser(), blogResponse.getUser());
        assertEquals(blogDto.getContent(), blogResponse.getContent());
        assertEquals(blogDto.getCreatedAt(), blogResponse.getCreatedAt());
        // verify
        verify(blogRepository, times(1)).findByUser(user);
        verify(converter, times(1)).convertBlogEntityToDto(blog);
    }

    @Test
    void testUpdateBlog_WithValidData_ShouldUpdateBlog() {
        // when
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(converter.convertBlogDtoToEntity(blogDto)).thenReturn(blog);
        when(blogRepository.save(blog)).thenReturn(blog);
        when(converter.convertBlogEntityToDto(blog)).thenReturn(blogDto);
        // then
        var actual = blogService.updateBlog(UUID.fromString(userId), blogId, blogDto);
        assertEquals(blogDto.getBlogId(), actual.getBlogId());
        assertEquals(blogDto.getTitle(), actual.getTitle());
        assertEquals(blogDto.getUser(), actual.getUser());
        assertEquals(blogDto.getContent(), actual.getContent());
        assertEquals(blogDto.getCreatedAt(), actual.getCreatedAt());
        // verify
        verify(blogRepository, times(1)).findById(blogId);
        verify(converter, times(1)).convertBlogDtoToEntity(blogDto);
        verify(blogRepository, times(1)).save(blog);
        verify(converter, times(1)).convertBlogEntityToDto(blog);
    }

    @Test
    void testUpdateBlog_WithInValidUserData_ShouldThrowBlogNotFoundException() {
        // when
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        // then
        assertThrows(
                BlogNotFoundException.class,
                () -> blogService.updateBlog(UUID.randomUUID(), blogId, blogDto)
        );
    }

    @Test
    void testUpdateBlog_WithInValidData_ShouldThrowBlogNotFoundException() {
        // when
        when(blogRepository.findById(UUID.randomUUID())).thenThrow(BlogNotFoundException.class);
        // then
        assertThrows(
                BlogNotFoundException.class,
                () -> blogService.updateBlog(UUID.fromString(userId), blogId, blogDto)
        );
    }

    @Test
    void testDeleteBlog_WithValidData_ShouldDeleteBlog() {
        // when
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        doNothing().when(blogRepository).deleteById(blogId);
        // then
        blogService.deleteBlog(blogId);
        // verify
        verify(blogRepository, times(1)).findById(blogId);
        verify(blogRepository, times(1)).deleteById(blogId);
    }

    @Test
    void testDeleteBlog_WithValidInData_ShouldThrowBlogNotFoundException() {
        assertThrows(
                BlogNotFoundException.class,
                () -> blogService.deleteBlog(UUID.randomUUID())
        );
    }
}