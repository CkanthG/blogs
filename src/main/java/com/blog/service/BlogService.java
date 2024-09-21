package com.blog.service;

import com.blog.converter.BlogEntityToDtoConverter;
import com.blog.entities.Blog;
import com.blog.entities.User;
import com.blog.exceptions.BlogNotFoundException;
import com.blog.models.BlogDto;
import com.blog.repositories.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This service class is used to handle all business logic of blogs.
 */
@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final BlogEntityToDtoConverter blogEntityToDtoConverter;

    /**
     * This method is used to create a blog.
     * @param blogDto used to create a blog.
     * @return created blog data.
     */
    public BlogDto createBlog(BlogDto blogDto) {
        Blog blog = blogRepository.save(blogEntityToDtoConverter.convertBlogDtoToEntity(blogDto));
        return blogEntityToDtoConverter.convertBlogEntityToDto(blog);
    }

    /**
     * This method is used to find blogs by user.
     * @param user is to find blogs.
     * @return list of user blogs.
     */
    public List<BlogDto> findBlogsByUser(User user) {
        return blogRepository.findByUser(user).stream().map(
                blogEntityToDtoConverter::convertBlogEntityToDto
        ).toList();
    }

    /**
     * This method is used to update blog.
     * @param userId is to identify the user blogs.
     * @param blogId is to identify the specific blog to update.
     * @param blog is to update the blog.
     * @return updated blog data.
     * @throws BlogNotFoundException if no matched data found to update.
     */
    public BlogDto updateBlog(UUID userId, UUID blogId, BlogDto blog) {
        Blog blogObject = getBlogObject(blogId);
        if (blogObject != null && blogObject.getUser().getUserId().equals(userId)) {
            Blog result = blogRepository.save(blogEntityToDtoConverter.convertBlogDtoToEntity(blog));
            return blogEntityToDtoConverter.convertBlogEntityToDto(result);
        } else {
            throw new BlogNotFoundException("No Blogs found for the user : " + userId);
        }
    }

    /**
     * This method is used to get blog by id.
     * @param blogId is to identify the blog.
     * @return matched blog data.
     * @throws BlogNotFoundException is not matched data found.
     */
    private Blog getBlogObject(UUID blogId) {
        return blogRepository.findById(blogId).orElseThrow(
                () -> new BlogNotFoundException("Blog not found with id " + blogId)
        );
    }

    /**
     * This method is used to delete blog by id.
     * @param blogId to identify the blog to delete.
     */
    public void deleteBlog(UUID blogId) {
        getBlogObject(blogId);
        blogRepository.deleteById(blogId);
    }
}
