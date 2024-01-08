package com.lakshay.blogbackend.entity;

import com.lakshay.blogbackend.error.custom_error.sign_up.SignUpException;
import com.lakshay.blogbackend.error.custom_error.sign_up.enums.SignUpExceptions;
import com.lakshay.blogbackend.utilities.Utilities;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@Document
public class User {
    @Id
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    @Email(message = "Invalid email format")
    private String email;
    private String password;

    public static User validate(String id, String firstname, String middleName, String lastname, String username, String email, String password) {
        if (Utilities.isValidEmail(email))
            return new User(id, firstname, middleName, lastname, username, email, Utilities.hashPassword(password));
        else
            throw new SignUpException(SignUpExceptions.INVALID_EMAIL_ID.getCode(), "Email id can either be null or a valid email");
    }
}
