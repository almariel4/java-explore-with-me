package ru.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IncorrectRequestEcxeption extends RuntimeException {

    private ApiError apiError;

    public IncorrectRequestEcxeption(ApiError apiError) {
        this.apiError = apiError;
    }
}
