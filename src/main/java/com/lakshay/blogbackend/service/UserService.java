package com.lakshay.blogbackend.service;

import com.lakshay.blogbackend.dto.UserDTO;
import com.lakshay.blogbackend.entity.User;
import com.lakshay.blogbackend.exception.custom_exception.user_exception.UserException;
import com.lakshay.blogbackend.exception.custom_exception.user_exception.UserExceptionCodes;
import com.lakshay.blogbackend.repository.UserRepository;
import com.lakshay.blogbackend.utilities.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mappers mappers;

    public UserDTO signupService(UserDTO userDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        if (optionalUser.isPresent())
            throw new UserException(UserExceptionCodes.USERNAME_ALREADY_EXIST.getCode(), "Username " + userDto.getUsername() + " already exists. Please retry another username.");
        optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent())
            throw new UserException(UserExceptionCodes.EMAIL_ALREADY_EXIST.getCode(), "Email " + userDto.getEmail() + " already exists. Please retry another email.");
        User user = User.validate(userDto.getId(), userDto.getFirstName(), userDto.getMiddleName(), userDto.getLastname(), userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        user = userRepository.save(user);
        userDto = mappers.convertUserToUserDTO(user);
        userDto.setPassword(null);
        return userDto;
    }
}
