package com.lakshay.blogbackend.exception.custom_exception.user_exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final String exceptionCode;

    public UserException(String exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
