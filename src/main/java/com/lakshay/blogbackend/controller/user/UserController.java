package com.lakshay.blogbackend.controller.user;

import com.lakshay.blogbackend.dto.LoginDTO;
import com.lakshay.blogbackend.dto.UserDTO;
import com.lakshay.blogbackend.entity.User;
import com.lakshay.blogbackend.exception.custom_exception.user_exception.UserException;
import com.lakshay.blogbackend.service.Authenticate;
import com.lakshay.blogbackend.service.UserService;
import com.lakshay.blogbackend.utilities.CookieUtils;
import com.lakshay.blogbackend.utilities.Mappers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserController {
    private static final String TIMESTAMP = "timestamp";
    private static final String MESSAGE = "message";
    private static final String ERROR_CODE = "errorCode";

    @Autowired
    private UserService userService;
    @Autowired
    private Authenticate authenticate;
    @Autowired
    private Mappers mappers;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody UserDTO userDto) {
        try {
            UserDTO user = userService.signupService(userDto);
            return ResponseEntity.ok(user);
        } catch (UserException exception) {
            log.error(exception);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(TIMESTAMP, LocalDateTime.now().toString());
            errorResponse.put(ERROR_CODE, exception.getExceptionCode());
            errorResponse.put(MESSAGE, exception.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception exception) {
            log.error(exception);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(TIMESTAMP, LocalDateTime.now().toString());
            errorResponse.put(MESSAGE, exception.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = authenticate.verifyLogin(loginDTO);
            CookieUtils.refreshUserCookie(user.getUsername(), request, response); // Implement token generation logic
            UserDTO userDTO = mappers.convertUserToUserDTO(user);
            userDTO.setPassword(null);
            return ResponseEntity.ok(userDTO);
        } catch (UserException exception) {
            log.error(exception);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(TIMESTAMP, LocalDateTime.now().toString());
            errorResponse.put(ERROR_CODE, exception.getExceptionCode());
            errorResponse.put(MESSAGE, exception.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception exception) {
            log.error(exception);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(TIMESTAMP, LocalDateTime.now().toString());
            errorResponse.put(MESSAGE, exception.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
