package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.dtos.LoginDTO;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.dtos.requests.UserRequest;
import com.nguyenvannhat.library.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestParam String username, @RequestParam String password) {
        LoginDTO loginDTO = userService.login(username, password);
        return ResponseEntity.ok(loginDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRequest userRequest) {
        UserDTO userDTO = userService.register(userRequest);
        return ResponseEntity.ok(userDTO);
    }
}
