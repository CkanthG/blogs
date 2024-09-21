package com.blog.repositories;

import com.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * This repository interface used for managing User entities in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Finds User entry associated with the given username.
     *
     * @param username used to be retrieved user.
     * @return a user entity associated with the provided username.
     */
    Optional<User> findByUsername(String username);
}

