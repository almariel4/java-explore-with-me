package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(StatsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleStatsException(StatsException e) {
        log.warn(e.getMessage());
        return new ApiError(e.getMessage(), "Действие невозможно.", HttpStatus.BAD_REQUEST.getReasonPhrase(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
