package com.lakshay.blogbackend.error.custom_error.sign_up.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignUpExceptionCodes {
    INVALID_EMAIL_ID("INVALID_EMAIL_ID"),
    EMAIL_ALREADY_EXIST("EMAIL_ALREADY_EXIST"),
    USERNAME_ALREADY_EXIST("USERNAME_ALREADY_EXIST");
    private final String code;
}
