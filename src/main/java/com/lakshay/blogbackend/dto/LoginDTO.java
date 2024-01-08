package com.lakshay.blogbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "Username and email can't be empty.")
    private String usernameOrEmail;
    @NotBlank(message = "Password can't be empty.")
    private String password;
}
