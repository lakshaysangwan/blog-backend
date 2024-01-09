package com.lakshay.blogbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class PostDTO {
    @NotBlank(message = "Post title can't be empty.")
    private String title;
    @NotBlank(message = "Post body can't be empty.")
    private String body;
    @NotBlank(message = "Author can't be empty.")
    private String username;
}
