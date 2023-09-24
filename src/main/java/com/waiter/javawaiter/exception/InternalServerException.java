package com.waiter.javawaiter.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
        log.error(message);
    }
}
