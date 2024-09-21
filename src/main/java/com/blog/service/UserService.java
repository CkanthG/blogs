package com.blog.service;

import com.blog.converter.UserEntityToDtoConverter;
import com.blog.entities.User;
import com.blog.exceptions.UserNotFoundException;
import com.blog.models.UserDto;
import com.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This service class is used to handle all business logic of users.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserEntityToDtoConverter userEntityToDtoConverter;

    /**
     * This method is used to create a user.
     * @param userDto is used to create a user.
     * @return created user.
     */
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.save(userEntityToDtoConverter.convertUserDtoToEntity(userDto));
        return userEntityToDtoConverter.convertUserEntityToDto(user);
    }

    /**
     * This method is used to find out user by username.
     * @param username to identify the user.
     * @return user.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * This method is used to find all users.
     * @return list of all users.
     */
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(
                userEntityToDtoConverter::convertUserEntityToDto
        ).toList();
    }

    /**
     * This method is used to delete user by id.
     * @param id is to identify the user to delete.
     * @throws UserNotFoundException if user not found.
     */
    public void deleteUser(UUID id) {
        userRepository.findById(id).ifPresentOrElse(
                user -> userRepository.deleteById(id),
                () -> { throw new UserNotFoundException("User not found with id : " + id); }
        );
    }
}
