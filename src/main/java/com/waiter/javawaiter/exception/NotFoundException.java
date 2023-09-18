package com.waiter.javawaiter.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс для отлова ошибки если сущность блюда или официанта не найдена
 */
@Slf4j
public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
        log.error(message);
    }
}