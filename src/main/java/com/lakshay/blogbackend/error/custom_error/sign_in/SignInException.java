package com.lakshay.blogbackend.error.custom_error.sign_in;

import lombok.Getter;

@Getter
public class SignInException extends RuntimeException {
    private final String exceptionCode;

    public SignInException(String exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
