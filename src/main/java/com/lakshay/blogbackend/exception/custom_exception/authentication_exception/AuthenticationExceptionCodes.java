package com.lakshay.blogbackend.exception.custom_exception.authentication_exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationExceptionCodes {
    INVALID_TOKEN("INVALID_TOKEN"),
    UNAUTHORIZED("UNAUTHORIZED");
    private final String code;
}
