package com.tweetapp.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BadCredentialException extends RuntimeException {

    /**
     * Constructs a new runtime exception with null as its detail message.
     **/
    public BadCredentialException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message the detail message.
     **/
    public BadCredentialException(String message) {
        super(message);
        log.error(message);
    }

    /**
     * Constructs a new runtime exception with the specified cause anf detail message.
     *
     * @param message
     * @param cause
     */
    public BadCredentialException(String message, Throwable cause) {
        super(message, cause);
        log.error(message);
    }

}
