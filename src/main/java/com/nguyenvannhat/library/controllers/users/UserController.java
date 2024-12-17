package com.nguyenvannhat.library.controllers.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.responses.LoginResponse;
import com.nguyenvannhat.library.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public CustomResponse<?> register(@RequestBody UserDTO userDTO) {
        try {
            userService.register(userDTO);
            return new CustomResponse<>(HttpStatus.CREATED, "User created successfully", userDTO);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/login")
    public CustomResponse<?> login(@RequestBody UserDTO userDTO) {
        try {
            LoginResponse loginResponse = userService.login(userDTO.getUsername(), userDTO.getPassword());
            return new CustomResponse<>(HttpStatus.OK, "User logged in successfully", loginResponse);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST, "Invalid username/password", null);
        }
    }

    @GetMapping("/information")
    public CustomResponse<?> getUserInformation() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findByUsername(auth.getName());
            UserDTO userDTO = new UserDTO(
                    user.getUsername(),
                    null, // Không trả password vì lý do bảo mật
                    user.getFullName(),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    user.getIdentityNumber(),
                    user.getBirthDay(),
                    user.getAge(),
                    user.getAddress()
            );

            return new CustomResponse<>(
                    HttpStatus.OK,
                    "User logged in successfully",
                    userDTO
            );
        } catch (Exception e) {
            return new CustomResponse<>(
                    HttpStatus.BAD_REQUEST,
                    "Invalid username/password",
                    null
            );
        }
    }

}