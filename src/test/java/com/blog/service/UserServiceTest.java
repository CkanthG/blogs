package com.blog.service;

import com.blog.converter.UserEntityToDtoConverter;
import com.blog.entities.User;
import com.blog.exceptions.UserNotFoundException;
import com.blog.models.UserDto;
import com.blog.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    @Mock
    private UserEntityToDtoConverter converter;
    private User user;
    private UserDto userDto;
    private final UUID userId = UUID.randomUUID();
    private final String username = "Sreekanth";
    private final LocalDateTime createdAt = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // given
        String email = "sree@gmail.com";
        String passwordHash = "39fc2d7152bf2b2bbed41b8e942894f5505896107aa60dd4a069f907e671b011";
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
    }

    @Test
    void testCreateUser_WithValidData_ShouldCreateUser() {
        // when
        when(converter.convertUserDtoToEntity(userDto)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(converter.convertUserEntityToDto(user)).thenReturn(userDto);
        // then
        var actual = service.createUser(userDto);
        assertEquals(user.getCreatedAt(), actual.getCreatedAt());
        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getPasswordHash(), actual.getPasswordHash());
        assertEquals(user.getEmail(), actual.getEmail());
        // verify
        verify(converter, times(1)).convertUserDtoToEntity(userDto);
        verify(repository, times(1)).save(user);
        verify(converter, times(1)).convertUserEntityToDto(user);
    }

    @Test
    void testFindByUsername_WithValidData_ShouldReturnUser() {
        // when
        when(repository.findByUsername(username)).thenReturn(Optional.of(user));
        // then
        var actual = service.findByUsername(username);
        assert actual.isPresent();
        var response = actual.get();
        assertEquals(user.getUserId(), response.getUserId());
        assertEquals(user.getCreatedAt(), response.getCreatedAt());
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getPasswordHash(), response.getPasswordHash());
        assertEquals(user.getEmail(), response.getEmail());
        // verify
        verify(repository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsername_WithInValidData_ShouldReturnEmpty() {
        var actual = service.findByUsername(username);
        assertTrue(actual.isEmpty());
    }

    @Test
    void testFindAllUsers_WithValidData_ShouldReturnUsers() {
        // when
        when(repository.findAll()).thenReturn(List.of(user));
        when(converter.convertUserEntityToDto(user)).thenReturn(userDto);
        // then
        var actual = service.findAllUsers();
        assert !actual.isEmpty();
        var response = actual.get(0);
        assertEquals(userDto.getCreatedAt(), response.getCreatedAt());
        assertEquals(userDto.getUsername(), response.getUsername());
        assertEquals(userDto.getPasswordHash(), response.getPasswordHash());
        assertEquals(userDto.getEmail(), response.getEmail());
        // verify
        verify(repository, times(1)).findAll();
        verify(converter, times(1)).convertUserEntityToDto(user);
    }

    @Test
    void testDeleteUser_WithValidData_ShouldDeleteUser() {
        // when
        when(repository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(repository).deleteById(userId);
        // then
        service.deleteUser(userId);
        // verify
        verify(repository, times(1)).findById(userId);
        verify(repository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_WithInValidData_ShouldThrowUserNotFoundException() {
        assertThrows(
                UserNotFoundException.class,
                () -> service.deleteUser(UUID.randomUUID())
        );
    }
}