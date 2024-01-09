package com.lakshay.blogbackend.exception.custom_exception.user_exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserExceptionCodes {
    INVALID_EMAIL_ID("INVALID_EMAIL_ID"),
    EMAIL_ALREADY_EXIST("EMAIL_ALREADY_EXIST"),
    USERNAME_ALREADY_EXIST("USERNAME_ALREADY_EXIST"),
    WRONG_PASSWORD("WRONG_PASSWORD"),
    NO_SUCH_USER("NO_SUCH_USER");
    private final String code;
}
