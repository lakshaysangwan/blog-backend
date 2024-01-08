package com.lakshay.blogbackend.error.custom_error.token_validation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationExceptionCodes {
    INVALID_TOKEN("INVALID_TOKEN"),
    UNAUTHORIZED("UNAUTHORIZED");
    private final String code;
}
