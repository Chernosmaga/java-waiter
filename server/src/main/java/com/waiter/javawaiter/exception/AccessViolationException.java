package com.waiter.javawaiter.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccessViolationException extends RuntimeException {
    public AccessViolationException(String message) {
        super(message);
        log.error(message);
    }
}
