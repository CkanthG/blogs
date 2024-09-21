package com.blog.controller;

import com.blog.entities.User;
import com.blog.models.UserDto;
import com.blog.repositories.UserRepository;
import com.blog.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {

    @Autowired
    private UserService service;
    @LocalServerPort
    private int port;
    private String userUrl;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;
    private UserDto userDto;
    private final LocalDateTime createdAt = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        userUrl = "http://localhost:" + port + "/users";
        String email = "sree@gmail.com";
        String passwordHash = "39fc2d7152bf2b2bbed41b8e942894f5505896107aa60dd4a069f907e671b011";
        String username = "Sreekanth";
        userDto = UserDto.builder()
                .createdAt(createdAt)
                .passwordHash(passwordHash)
                .username(username)
                .email(email)
                .build();
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser_WithValidData_ShouldGiveCreatedStatusCode() {
        ResponseEntity<UserDto> responseEntity = testRestTemplate.postForEntity(
                userUrl + "/register",
                userDto,
                UserDto.class
        );
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testGetAllUsers_WithValidData_ShouldGiveOkStatusCode() {
        ResponseEntity<UserDto> responseEntity = testRestTemplate.postForEntity(
                userUrl + "/register",
                userDto,
                UserDto.class
        );
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        ResponseEntity<UserDto[]> response = testRestTemplate.getForEntity(
                userUrl, UserDto[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteUser_WithValidData_ShouldGiveNoContentStatusCode() {
        ResponseEntity<UserDto> responseEntity = testRestTemplate.postForEntity(
                userUrl + "/register",
                userDto,
                UserDto.class
        );
        UUID savedUserId = Objects.requireNonNull(responseEntity.getBody()).getUserId();
        ResponseEntity<Void> response = testRestTemplate.exchange(
                userUrl + "/" + savedUserId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}