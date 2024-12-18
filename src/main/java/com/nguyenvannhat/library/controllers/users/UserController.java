package com.nguyenvannhat.library.controllers.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.responses.LoginResponse;
import com.nguyenvannhat.library.services.bookloan.BookLoanService;
import com.nguyenvannhat.library.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API for managing user operations") // Gắn nhãn cho toàn bộ Controller
public class UserController {
    private final UserService userService;
    private final BookLoanService bookLoanService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Allows a new user to register with required details")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) throws Exception {
        userService.register(userDTO);
        return CustomResponse.success(userDTO);
    }

    @GetMapping("/read/roles")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get user roles", description = "Fetch roles for the current user")
    public ResponseEntity<?> getRoles() throws Exception {
        List<GrantedAuthority> roles = userService.getAuthorities();
        return CustomResponse.success(roles);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate a user and retrieve JWT token")
    public LoginResponse login(@RequestBody UserDTO userDTO) throws Exception {
        return userService.login(userDTO.getUsername(), userDTO.getPassword());
    }

    @GetMapping("/read/information")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get user information", description = "Retrieve the currently authenticated user's details")
    public ResponseEntity<?> getUserInformation() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        return CustomResponse.success(user);
    }

    @PutMapping("/update/blackList")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Update user blacklist", description = "Add a user to the blacklist")
    public ResponseEntity<?> updateBlackList(@RequestBody UserDTO userDTO) throws Exception {
        User user = userService.findByUsername(userDTO.getUsername());
        bookLoanService.addUserBlackList(user);
        return CustomResponse.success("User blacklist updated successfully!!!");
    }
}
