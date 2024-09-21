package com.blog.exceptions;

/**
 * This class is used to handle blog not found exception.
 */
public class BlogNotFoundException extends RuntimeException{
    public BlogNotFoundException(String message) {
        super(message);
    }
}
