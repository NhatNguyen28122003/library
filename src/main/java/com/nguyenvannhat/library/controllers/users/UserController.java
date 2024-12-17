package com.nguyenvannhat.library.controllers.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.responses.LoginResponse;
import com.nguyenvannhat.library.services.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public CustomResponse<?> register(@RequestBody UserDTO userDTO) throws Exception {
        userService.register(userDTO);
        return new CustomResponse<>(HttpStatus.CREATED, "User created successfully", userDTO);
    }
    @GetMapping("/read/roles")
    @PreAuthorize("fileRole()")
    public CustomResponse<?> getRoles() throws Exception {
        List<GrantedAuthority> roles = userService.getAuthorities();
        return new CustomResponse<>(HttpStatus.ACCEPTED,"ok",roles);
    }
    @PostMapping("/login")
    public CustomResponse<?> login(@RequestBody UserDTO userDTO) throws Exception {
        LoginResponse loginResponse = userService.login(userDTO.getUsername(), userDTO.getPassword());
        return new CustomResponse<>(HttpStatus.OK, "User logged in successfully", loginResponse);
    }

    @GetMapping("/information")
    public CustomResponse<?> getUserInformation() throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        return new CustomResponse<>(HttpStatus.OK, "Get my information success =)))", user);
    }
}
