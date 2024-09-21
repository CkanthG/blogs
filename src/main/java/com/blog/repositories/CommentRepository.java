package com.blog.repositories;

import com.blog.entities.Blog;
import com.blog.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * This repository interface used for managing Comment entities in the database.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    /**
     * Finds all Comment entries associated with the given Blog.
     *
     * @param blog used to be retrieved all blog comments.
     * @return a list of comment entities associated with the provided blog.
     */
    List<Comment> findByBlog(Blog blog);
}

