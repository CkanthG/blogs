package com.blog.controller;

import com.blog.entities.User;
import com.blog.models.BlogDto;
import com.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.UUID;

/**
 * This controller class is responsible for handle all blog related endpoints.
 */
@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    /**
     * This method is used to create a blog.
     * @param blog is used to create a new blog.
     * @return created blog data.
     */
    @PostMapping
    public ResponseEntity<BlogDto> createBlog(@RequestBody BlogDto blog) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.createBlog(blog));
    }

    /**
     * This method is used to update the blog.
     * @param userId to identify which user created this blog.
     * @param blogId to identify blog to update it.
     * @param blog to update the blog information.
     * @return updated blog data.
     */
    @PutMapping("/{userId}/{blogId}")
    public ResponseEntity<BlogDto> updateBlog(@PathVariable UUID userId, @PathVariable UUID blogId, @RequestBody BlogDto blog) {
        return ResponseEntity.ok(blogService.updateBlog(userId, blogId, blog));
    }

    /**
     * This method is used to return all blogs created by specific user.
     * @param userId to identify all blogs related to user.
     * @return all user blogs.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BlogDto>> getBlogsByUser(@PathVariable UUID userId) {
        User user = new User();
        user.setUserId(userId);
        return ResponseEntity.ok(blogService.findBlogsByUser(user));
    }

    /**
     * This method is used to delete the blog by its id.
     * @param blogId to identify the blog to delete it.
     * @return no content will be returned except 204 status code.
     */
    @DeleteMapping("/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable UUID blogId) {
        blogService.deleteBlog(blogId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

