package com.lakshay.blogbackend.error.custom_error.sign_up;

import lombok.Getter;

@Getter
public class SignUpException extends RuntimeException {
    private final String exceptionCode;

    public SignUpException(String exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
