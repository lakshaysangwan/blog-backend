package com.lakshay.blogbackend.utilities;

import com.lakshay.blogbackend.dto.UserDTO;
import com.lakshay.blogbackend.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Mappers {
    @Autowired
    private ModelMapper modelMapper;

    public UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
