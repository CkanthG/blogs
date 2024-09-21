package com.blog.converter;

import com.blog.entities.Blog;
import com.blog.exceptions.BlogNotFoundException;
import com.blog.models.BlogDto;
import com.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class is responsible for converting blog entity to blog dto and vice versa.
 */
@Component
@RequiredArgsConstructor
public class BlogEntityToDtoConverter {

    private final CommentEntityToDtoConverter commentEntityToDtoConverter;
    private final UserRepository userRepository;

    /**
     * This method is used to convert blog dto to blog entity.
     * @param blogDto to convert blog entity.
     * @return converted blog entity.
     * @throws BlogNotFoundException if blog is not found.
     */
    public Blog convertBlogDtoToEntity(BlogDto blogDto) {
        return Blog.builder()
                .blogId(blogDto.getBlogId())
                .user(
                        userRepository.findById(UUID.fromString(blogDto.getUser())).orElseThrow(
                                () -> new BlogNotFoundException("Blog not found by user id : " + blogDto.getUser())
                        )
                )
                .title(blogDto.getTitle())
                .content(blogDto.getContent())
                .createdAt(blogDto.getCreatedAt())
                .comments(
                        blogDto.getComments() != null ? blogDto.getComments().stream().map(
                                commentEntityToDtoConverter::convertCommentDtoToEntity
                        ).collect(Collectors.toSet()) : null
                )
                .build();
    }

    /**
     * This method is used to convert blog entity to blog dto.
     * @param blog to convert blog dto.
     * @return converted blog dto.
     */
    public BlogDto convertBlogEntityToDto(Blog blog) {
        return BlogDto.builder()
                .blogId(blog.getBlogId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .createdAt(blog.getCreatedAt())
                .user(blog.getUser().getUserId().toString())
                .build();
    }
}
