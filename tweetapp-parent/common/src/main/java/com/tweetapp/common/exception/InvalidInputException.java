package com.tweetapp.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidInputException extends RuntimeException {

    /**
     * Constructs a new runtime exception with null as its detail message.
     **/
    public InvalidInputException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message the detail message.
     **/
    public InvalidInputException(String message) {
        super(message);
        log.error(message);
    }

    /**
     * Constructs a new runtime exception with the specified cause anf detail message.
     *
     * @param message
     * @param cause
     */
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
        log.error(message);
    }

}
