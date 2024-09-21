package com.blog.repositories;

import com.blog.entities.Blog;
import com.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * This repository interface used for managing Blog entities in the database.
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, UUID> {
    /**
     * Finds all Blog entries associated with the given User.
     *
     * @param user the user whose blogs are to be retrieved.
     * @return a list of Blog entities associated with the provided user.
     */
    List<Blog> findByUser(User user);
}

