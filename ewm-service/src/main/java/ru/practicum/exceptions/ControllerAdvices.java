package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ControllerAdvices {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleException(ConstraintViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT.toString(), ex.getCause().toString(), ex.getMessage(),
                String.format(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectRequestEcxeption.class)
    public ResponseEntity<ApiError> handleException(IncorrectRequestEcxeption ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.toString(), ex.getCause().toString(), ex.getMessage(),
                String.format(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConditionsException.class)
    public ResponseEntity<ApiError> handleException(ConditionsException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.toString(), ex.getCause().toString(), ex.getMessage(),
                String.format(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
