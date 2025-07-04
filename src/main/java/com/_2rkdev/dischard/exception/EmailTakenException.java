package com._2rkdev.dischard.exception;

/**
 * Exception thrown when an email is about to be associated with two different user accounts
 */
public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String message) {
        super(message);
    }
}
