package com.lakshay.blogbackend.service;

import com.lakshay.blogbackend.dto.LoginDTO;
import com.lakshay.blogbackend.entity.User;
import com.lakshay.blogbackend.exception.custom_exception.user_exception.UserException;
import com.lakshay.blogbackend.exception.custom_exception.user_exception.UserExceptionCodes;
import com.lakshay.blogbackend.repository.UserRepository;
import com.lakshay.blogbackend.utilities.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Authenticate {
    @Autowired
    private UserRepository userRepository;

    public User verifyLogin(LoginDTO loginDTO) {
        Optional<User> user = userRepository.findByUsernameOrEmail(loginDTO.getUsernameOrEmail());
        if (user.isEmpty())
            throw new UserException(UserExceptionCodes.NO_SUCH_USER.getCode(), "No user with such username or emailId");
        if (Utilities.verifyPassword(loginDTO.getPassword(), user.get().getPassword())) {
            return user.get();
        }
        throw new UserException(UserExceptionCodes.WRONG_PASSWORD.getCode(), "Wrong password. Please try again.");
    }

    public void verifyUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new UserException(UserExceptionCodes.NO_SUCH_USER.getCode(), "No such user for making the post");
    }
}
