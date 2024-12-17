package com.nguyenvannhat.library.controllers.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.responses.LoginResponse;
import com.nguyenvannhat.library.services.bookloan.BookLoanService;
import com.nguyenvannhat.library.services.users.UserService;
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
public class UserController {
    private final UserService userService;
    private final BookLoanService bookLoanService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) throws Exception {
        userService.register(userDTO);
        return CustomResponse.success(userDTO);
    }
    @GetMapping("/read/roles")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> getRoles() throws Exception {
        List<GrantedAuthority> roles = userService.getAuthorities();
        return CustomResponse.success(roles);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody UserDTO userDTO) throws Exception {
        return userService.login(userDTO.getUsername(), userDTO.getPassword());

    }

    @GetMapping("/information")
    public ResponseEntity<?> getUserInformation() throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        return CustomResponse.success(user);
    }

    @PutMapping("/update/blackList")
    public ResponseEntity<?> updateBlackList(@RequestBody UserDTO userDTO) throws Exception {
        User user = userService.findByUsername(userDTO.getUsername());
        bookLoanService.addUserBlackList(user);
        return CustomResponse.success("User blacklist updated successfully!!!");
    }
}
