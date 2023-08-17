package ru.practicum.evmservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private ApiError apiError;

    public NotFoundException(ApiError apiError) {
        this.apiError = apiError;
        apiError.getStatus();
        apiError.getReason();
        apiError.getMessage();
        apiError.getTimestamp();
    }

    public NotFoundException() {
    }
}
