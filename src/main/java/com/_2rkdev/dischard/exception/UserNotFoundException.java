package com._2rkdev.dischard.exception;

/**
 * For when a user cannot be found from the data provided by a request, not to be used in authentications to keep them opaque
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
