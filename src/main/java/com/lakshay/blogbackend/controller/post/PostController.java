package com.lakshay.blogbackend.controller.post;

import com.lakshay.blogbackend.dto.PostDTO;
import com.lakshay.blogbackend.exception.custom_exception.authentication_exception.AuthenticationException;
import com.lakshay.blogbackend.exception.custom_exception.user_exception.UserException;
import com.lakshay.blogbackend.service.PostService;
import com.lakshay.blogbackend.utilities.CookieUtils;
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
@RequestMapping("/post")
public class PostController {
    private static final String TIMESTAMP = "timestamp";
    private static final String MESSAGE = "message";
    private static final String ERROR_CODE = "errorCode";

    @Autowired
    private PostService postService;

    @PostMapping("/add")
    public ResponseEntity<Object> addPost(@Valid @RequestBody PostDTO postDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            CookieUtils.verifyRequest(postDTO.getUsername(), httpServletRequest);
            CookieUtils.refreshUserCookie(postDTO.getUsername(), httpServletRequest, httpServletResponse);
            return ResponseEntity.ok(postService.addPost(postDTO));
        } catch (UserException exception) {
            log.error(exception);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(TIMESTAMP, LocalDateTime.now().toString());
            errorResponse.put(ERROR_CODE, exception.getExceptionCode());
            errorResponse.put(MESSAGE, exception.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (AuthenticationException exception) {
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
