package com.waiter.javawaiter.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationViolationException extends RuntimeException {

    public ValidationViolationException(final String message) {
        super(message);
        log.error(message);
    }
}
