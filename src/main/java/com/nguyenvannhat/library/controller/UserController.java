package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @GetMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read")
    public CustomResponse<List<UserDTO>> findAllUsers(HttpServletRequest request) {
        return userService.findAllUsers();
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/{id}")
    public CustomResponse<User> findUserById(HttpServletRequest request, @PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/phoneNumber/{phoneNumber}")
    public CustomResponse<User> findUserByPhoneNumber(HttpServletRequest request, @PathVariable("phoneNumber") String phoneNumber) {
        return userService.findByPhoneNumber(phoneNumber);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/email/{email}")
    public CustomResponse<User> findUserByEmail(HttpServletRequest request, @PathVariable("email") String email) {
        return userService.findByEmail(email);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/identity/{identity}")
    public CustomResponse<User> findUsersByIdentity(HttpServletRequest request, @PathVariable("identity") String identity) {
        return userService.findByIdentity(identity);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/fullName/{fullName}")
    public CustomResponse<List<UserDTO>> findUsersByFullName(HttpServletRequest request, @PathVariable("fullName") String fullName) {
        return userService.findAllUsersFullName(fullName);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/age/{age}")
    public CustomResponse<List<UserDTO>> findUsersByAge(HttpServletRequest request, @PathVariable("age") Integer age) {
        return userService.findAllUsersByAge(age);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/address/{address}")
    public CustomResponse<List<UserDTO>> findUsersByAddress(HttpServletRequest request, @PathVariable("address") String address) {
        return userService.findAllUsersByAddress(address);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/birthDay/{birthday}")
    public CustomResponse<List<UserDTO>> findUsersByAddress(HttpServletRequest request, @PathVariable("birthday")LocalDate birthday) {
        return userService.findAllUsersByBirthday(birthday);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/{username}")
    public CustomResponse<User> findUserByUsername(HttpServletRequest request, @PathVariable("username") String username) {
        return userService.findByUserName(username);
    }

    @PreAuthorize("fileRole(#request)")
    @PutMapping("/update/{id}")
    public CustomResponse<User> updateUser(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO);
    }

    @PreAuthorize("fileRole(#request)")
    @DeleteMapping("/delete/{id}")
    public void findUserByUsername(HttpServletRequest request, @PathVariable("id") Long id) {
        userService.delete(id);
    }

}
