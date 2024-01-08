package com.lakshay.blogbackend.service;

import com.lakshay.blogbackend.dto.LoginDTO;
import com.lakshay.blogbackend.entity.User;
import com.lakshay.blogbackend.error.custom_error.sign_in.SignInException;
import com.lakshay.blogbackend.error.custom_error.sign_in.enums.SignInExceptionCodes;
import com.lakshay.blogbackend.repository.UserRepository;
import com.lakshay.blogbackend.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Authenticate {
    @Autowired
    private UserRepository userRepository;

    public User verifyUser(LoginDTO loginDTO) {
        Optional<User> user = userRepository.findByUsernameOrEmail(loginDTO.getUsernameOrEmail());
        if (user.isEmpty())
            throw new SignInException(SignInExceptionCodes.NO_SUCH_USER_FOUND.getCode(), "No user with such username or emailId");
        if (Utilities.verifyPassword(loginDTO.getPassword(), user.get().getPassword())) {
            return user.get();
        }
        throw new SignInException(SignInExceptionCodes.WRONG_PASSWORD.getCode(), "Wrong password. Please try again.");
    }
}
