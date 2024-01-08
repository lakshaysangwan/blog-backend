package com.lakshay.blogbackend.service;

import com.lakshay.blogbackend.dto.UserDTO;
import com.lakshay.blogbackend.entity.User;
import com.lakshay.blogbackend.error.custom_error.sign_up.SignUpException;
import com.lakshay.blogbackend.error.custom_error.sign_up.enums.SignUpExceptions;
import com.lakshay.blogbackend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO signupService(UserDTO userDto) {
        Optional<List<User>> userList = userRepository.findByUsername(userDto.getUsername());
        if (userList.isPresent() && !userList.get().isEmpty())
            throw new SignUpException(SignUpExceptions.USERNAME_ALREADY_EXIST.getCode(), "Username " + userDto.getUsername() + " already exists. Please retry another username.");
        userList = userRepository.findByEmail(userDto.getEmail());
        if (userList.isPresent() && !userList.get().isEmpty())
            throw new SignUpException(SignUpExceptions.EMAIL_ALREADY_EXIST.getCode(), "Email " + userDto.getEmail() + " already exists. Please retry another email.");
        User user = User.validate(userDto.getId(), userDto.getFirstName(), userDto.getMiddleName(), userDto.getLastname(), userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        user = userRepository.save(user);
        userDto = convertToDto(user);
        userDto.setPassword(null);
        return userDto;
    }
}
