package com.blog.controller;

import com.blog.entities.User;
import com.blog.models.BlogDto;
import com.blog.repositories.BlogRepository;
import com.blog.repositories.UserRepository;
import com.blog.service.BlogService;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogControllerIT extends AbstractTestContainer {

    @Autowired
    BlogService service;
    @LocalServerPort
    private int port;
    private String blogUrl;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlogRepository blogRepository;
    private User user;
    private User savedUser;
    private BlogDto blogDto;
    private String userId = UUID.randomUUID().toString();
    private final LocalDateTime createdAt = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        blogUrl = "http://localhost:" + port + "/blogs";
        String content = "My first blog content";

        String title = "MyBlog";
        String email = "sree@gmail.com";
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
        blogDto = BlogDto.builder()
                .title(title)
                .user(userId)
                .content(content)
                .createdAt(createdAt)
                .comments(Set.of())
                .build();
    }

    @AfterEach
    void cleanUp() {
        blogRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateBlog_WithValidData_ShouldGiveCreatedStatusCode() {
        blogDto.setUser(userId);
        ResponseEntity<BlogDto> blogDtoResponseEntity = testRestTemplate.postForEntity(blogUrl, blogDto, BlogDto.class);
        assertEquals(HttpStatus.CREATED, blogDtoResponseEntity.getStatusCode());
        var actual = blogDtoResponseEntity.getBody();
        assert actual != null;
        assertEquals(blogDto.getContent(), actual.getContent());
        assertEquals(blogDto.getCreatedAt(), actual.getCreatedAt());
        assertEquals(blogDto.getUser(), actual.getUser());
    }

    @Test
    void testUpdateBlog_WithValidData_ShouldGiveOkStatusCode() {
        blogDto.setUser(userId);
        ResponseEntity<BlogDto> blogDtoResponseEntity = testRestTemplate.postForEntity(blogUrl, blogDto, BlogDto.class);
        assertEquals(HttpStatus.CREATED, blogDtoResponseEntity.getStatusCode());
        var blogResponse = blogDtoResponseEntity.getBody();
        assert blogResponse != null;
        UUID updatedBlogId = blogResponse.getBlogId();
        String updatedTitle = "updated title";
        String updatedContent = "content updated";
        blogDto.setBlogId(updatedBlogId);
        blogDto.setTitle(updatedTitle);
        blogDto.setContent(updatedContent);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<BlogDto> entity = new HttpEntity<>(blogDto, httpHeaders);
        ResponseEntity<BlogDto> responseEntity = testRestTemplate.exchange(
                blogUrl + "/" + userId + "/" + updatedBlogId,
                HttpMethod.PUT,
                entity,
                BlogDto.class
        );
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        var actual = responseEntity.getBody();
        assert actual != null;
        assertEquals(updatedBlogId, actual.getBlogId());
        assertEquals(updatedTitle, actual.getTitle());
        assertEquals(updatedContent, actual.getContent());
    }

    @Test
    void testGetBlogsByUser_WithValidData_ShouldReturnOkStatusCode() {
        blogDto.setUser(userId);
        ResponseEntity<BlogDto> blogDtoResponseEntity = testRestTemplate.postForEntity(blogUrl, blogDto, BlogDto.class);
        assertEquals(HttpStatus.CREATED, blogDtoResponseEntity.getStatusCode());
        var blogResponse = blogDtoResponseEntity.getBody();
        assert blogResponse != null;
        ResponseEntity<BlogDto[]> responseEntity = testRestTemplate.getForEntity(blogUrl + "/user/" + userId, BlogDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteBlog_WithValidData_ShouldReturnNoContentStatusCode() {
        blogDto.setUser(userId);
        ResponseEntity<BlogDto> blogDtoResponseEntity = testRestTemplate.postForEntity(blogUrl, blogDto, BlogDto.class);
        assertEquals(HttpStatus.CREATED, blogDtoResponseEntity.getStatusCode());
        var blogResponse = blogDtoResponseEntity.getBody();
        assert blogResponse != null;
        UUID createdBlogId = blogResponse.getBlogId();
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(
                blogUrl + "/" + createdBlogId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}