package com.nguyenvannhat.library.controllers.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.respones.CustomResponse;
import com.nguyenvannhat.library.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    @GetMapping("/roles")
    public CustomResponse<?> getRoles() {
        try {
            return new CustomResponse<>(HttpStatus.ACCEPTED,"ok",userService.getAuthorities());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error",e);
        }
    }

    @PostMapping("/create")
    public CustomResponse<?> register(@RequestBody UserDTO userDTO) {
        try {
            userService.register(userDTO);
            return new CustomResponse<>(HttpStatus.CREATED,"User created successfully", userDTO);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST,e.getMessage(), null);
        }
    }
}




