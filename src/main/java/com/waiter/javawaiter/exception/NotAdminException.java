package com.waiter.javawaiter.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * Класс для отлова ошибки, если официант пытается создать блюдо
 */
@Slf4j
public class NotAdminException extends RuntimeException {

    public NotAdminException(final String message) {
        super(message);
        log.error(message);
    }
}
