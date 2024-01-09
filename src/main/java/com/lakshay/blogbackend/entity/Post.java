package com.lakshay.blogbackend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@RequiredArgsConstructor
@Document
public class Post {
    @Id
    private final String id;
    private final String postTitle;
    private final String postBody;
    private final String authorUsername;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;

    public static Post validate(String id, String postTitle, String postBody, String authorUsername) {
        return new Post(id, postTitle, postBody, authorUsername);
    }
}
