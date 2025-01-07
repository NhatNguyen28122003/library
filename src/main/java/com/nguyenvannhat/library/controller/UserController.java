package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final MessageSource messageSource;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public CustomResponse<User> register(@RequestBody UserDTO userDTO) {
        String message = messageSource.getMessage(Constant.SUCCESS_USER_CREATED, null, Locale.ENGLISH);
        log.info(message);
        return CustomResponse.success(HttpStatus.CREATED.value(), message, userService.register(userDTO));
    }

    @GetMapping("/login")
    public CustomResponse<String> login(@RequestBody UserDTO userDTO) {
        String message = messageSource.getMessage(Constant.SUCCESS_LOGIN, null, Locale.ENGLISH);
        log.info(message);
        return CustomResponse.success(HttpStatus.ACCEPTED.value(), message, userService.login(userDTO));
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read")
    public CustomResponse<List<UserDTO>> read(HttpServletRequest request) {
        String message = messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH);
        log.info(message);
        return CustomResponse.success(HttpStatus.OK.value(), message, userService.getAllUsers());
    }

    @PreAuthorize("fileRole(#request)")
    @PutMapping("/update")
    public CustomResponse<User> update(HttpServletRequest request, @RequestBody UserDTO userDTO) {
        String message = messageSource.getMessage(Constant.SUCCESS_USER_UPDATED, null, Locale.ENGLISH);
        return CustomResponse.success(HttpStatus.OK.value(), message, userService.update(userDTO));
    }

}
