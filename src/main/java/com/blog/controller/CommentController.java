package com.blog.controller;

import com.blog.entities.Blog;
import com.blog.models.CommentDto;
import com.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

/**
 * This controller class is responsible for all comment related endpoints.
 */
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * This method is used to create a comment.
     * @param comment is used to create a comment.
     * @return created comment.
     */
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto comment) {
        return ResponseEntity.ok(commentService.createComment(comment));
    }

    /**
     * This method will return a specific blog all comments.
     * @param blogId to identify the all comments related to blog.
     * @return all comments of a specific blog.
     */
    @GetMapping("/blog/{blogId}")
    public ResponseEntity<List<CommentDto>> getCommentsByBlog(@PathVariable UUID blogId) {
        Blog blog = new Blog();
        blog.setBlogId(blogId);
        return ResponseEntity.ok(commentService.findCommentsByBlog(blog));
    }
}

