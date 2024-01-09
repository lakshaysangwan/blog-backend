package com.lakshay.blogbackend.entity;

import com.lakshay.blogbackend.exception.custom_exception.user_exception.UserException;
import com.lakshay.blogbackend.exception.custom_exception.user_exception.UserExceptionCodes;
import com.lakshay.blogbackend.utilities.Utilities;
import jakarta.validation.constraints.Email;
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
public class User {
    @Id
    private final String id;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final String username;
    @Email(message = "Invalid email format")
    private final String email;
    private final String password;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;

    public static User validate(String id, String firstname, String middleName, String lastname, String username, String email, String password) {
        if (Utilities.isValidEmail(email))
            return new User(id, firstname, middleName, lastname, username, email, Utilities.hashPassword(password));
        else
            throw new UserException(UserExceptionCodes.INVALID_EMAIL_ID.getCode(), "Email id can either be null or a valid email");
    }
}
