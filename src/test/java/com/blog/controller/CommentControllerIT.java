package com.blog.controller;

import com.blog.entities.Blog;
import com.blog.entities.User;
import com.blog.models.CommentDto;
import com.blog.repositories.BlogRepository;
import com.blog.repositories.CommentRepository;
import com.blog.repositories.UserRepository;
import com.blog.service.CommentService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerIT extends AbstractTestContainer {

    @Autowired
    private CommentService service;
    @LocalServerPort
    private int port;
    private String commentUrl;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CommentRepository commentRepository;
    private User user;
    private User savedUser;
    private Blog savedBlog;
    private String userId = UUID.randomUUID().toString();
    private final LocalDateTime createdAt = LocalDateTime.now();
    private String blogId;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        commentUrl = "http://localhost:" + port + "/comments";
        String content = "My first blog content";

        String title = "MyBlog";
        String email = RandomGenerator.getDefault() + "sree@gmail.com";
        String passwordHash = "39fc2d7152bf2b2bbed41b8e942894f5505896107aa60dd4a069f907e671b011";
        String username = "sreekanth"+ RandomGenerator.getDefault();

        user = User.builder()
                .userId(UUID.fromString(userId))
                .username(username)
                .passwordHash(passwordHash)
                .createdAt(createdAt)
                .email(email)
                .build();
        savedUser = userRepository.save(user);
        userId = savedUser.getUserId().toString();
        Blog blog = Blog.builder()
                .user(savedUser)
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .comments(Set.of())
                .build();
        savedBlog = blogRepository.save(blog);
        blogId = savedBlog.getBlogId().toString();
        commentDto = new CommentDto(null, blogId, userId, content, createdAt);
    }

    @AfterEach
    void cleanUp() {
        commentRepository.deleteAll();
        blogRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateComment_WithValidData_ShouldGiveOkStatusCode() {
        ResponseEntity<CommentDto> responseEntity = testRestTemplate.postForEntity(
                commentUrl,
                commentDto,
                CommentDto.class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetCommentsByBlog_WithValidData_ShouldGiveOkStatusCode() {
        testRestTemplate.postForEntity(
                commentUrl,
                commentDto,
                CommentDto.class
        );
        ResponseEntity<CommentDto[]> restTemplateForEntity = testRestTemplate.getForEntity(commentUrl + "/blog/" + blogId, CommentDto[].class);
        assertEquals(HttpStatus.OK, restTemplateForEntity.getStatusCode());
    }
}
