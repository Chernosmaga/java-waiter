package com.waiter.javawaiter.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс для отлова ошибки если сущность блюда или официанта уже существует
 */
@Slf4j
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(final String message) {
        super(message);
        log.error(message);
    }
}
