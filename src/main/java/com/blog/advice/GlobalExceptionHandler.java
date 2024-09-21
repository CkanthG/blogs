package com.blog.advice;

import com.blog.exceptions.BlogNotFoundException;
import com.blog.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class handle all kind of exceptions and sends proper error message.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method is used to handle BlogNotFoundException
     * @param notFoundException throws when exception occurred.
     * @return response entity object with proper error message.
     */
    @ExceptionHandler({BlogNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<String> handleNotFoundExceptions(Exception notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
    }

    /**
     * This method is used to handle all Exception
     * @param exception throws when exception occurred.
     * @return response entity object with proper error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }
}
