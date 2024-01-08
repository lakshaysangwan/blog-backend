package com.lakshay.blogbackend.error.custom_error.sign_in.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignInExceptionCodes {
    NO_SUCH_USER_FOUND("NO_SUCH_USER_FOUND"),
    WRONG_PASSWORD("WRONG_PASSWORD");
    private final String code;
}
