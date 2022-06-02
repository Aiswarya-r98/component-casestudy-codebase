package com.tweetapp.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcesssingException extends RuntimeException {

    /**
     * Constructs a new runtime exception with null as its detail message.
     **/
    public ProcesssingException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message the detail message.
     **/
    public ProcesssingException(String message) {
        super(message);
        log.error(message);
    }

    /**
     * Constructs a new runtime exception with the specified cause anf detail message.
     *
     * @param message
     * @param cause
     */
    public ProcesssingException(String message, Throwable cause) {
        super(message, cause);
        log.error(message);
    }

}
