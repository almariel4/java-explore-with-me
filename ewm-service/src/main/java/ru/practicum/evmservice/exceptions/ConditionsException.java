package ru.practicum.evmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConditionsException extends RuntimeException {

    private ApiError apiError;

    public ConditionsException(ApiError apiError) {
        this.apiError = apiError;
        apiError.getStatus();
        apiError.getReason();
        apiError.getMessage();
        apiError.getTimestamp();
    }
}
