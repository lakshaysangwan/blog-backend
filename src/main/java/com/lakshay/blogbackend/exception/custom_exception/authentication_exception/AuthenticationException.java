package com.lakshay.blogbackend.exception.custom_exception.authentication_exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private final String exceptionCode;

    public AuthenticationException(String exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
