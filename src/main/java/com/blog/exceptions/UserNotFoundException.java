package com.blog.exceptions;

/**
 * This class is used to handle user not found exception.
 */
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
