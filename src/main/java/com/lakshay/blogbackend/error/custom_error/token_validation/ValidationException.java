package com.lakshay.blogbackend.error.custom_error.token_validation;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final String exceptionCode;

    public ValidationException(String exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
