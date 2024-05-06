package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public Map<String, String> handleNegativeCount(final IllegalArgumentException e) {
        log.info("IllegalArgumentException {}", e.getMessage());
        return Map.of("Передан отрицательный параметр count.", e.getMessage());
    }

    @ExceptionHandler
    public Map<String, String> handleNull(final NullPointerException e) {
        log.info("NullPointerException {}", e.getMessage());
        return Map.of("Значение не передано.", e.getMessage());
    }

    @ExceptionHandler
    public Map<String, String> handleError(final RuntimeException e) {
        log.info("RuntimeException {}", e.getMessage());
        return Map.of("Произошла ошибка!", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationError(final ValidationException e) {
        log.info("400 {}", e.getMessage());
        return Map.of("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundError(final NotFoundException e) {
        log.info("404 {}", e.getMessage());
        return Map.of("Ресурс не найден", e.getMessage());
    }
}
