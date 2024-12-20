package com.nguyenvannhat.library.controllers.users;

import com.nguyenvannhat.library.components.AppConfig;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.responses.LoginResponse;
import com.nguyenvannhat.library.responses.SuccessCode;
import com.nguyenvannhat.library.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AppConfig appConfig;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) throws Exception {
        userService.register(userDTO);
        return CustomResponse.success(HttpStatus.CREATED, SuccessCode.USER_CREATED, appConfig.messageSource(), userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserDTO userDTO) throws Exception {
        LoginResponse loginResponse = userService.login(userDTO.getUsername(), userDTO.getPassword());
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/information")
    public ResponseEntity<CustomResponse<List<GrantedAuthority>>> getUser() throws Exception {
        List<GrantedAuthority> grantedAuthorities = userService.getAuthorities();
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), grantedAuthorities);
    }

    @GetMapping("/read")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<User>>> readAllUsers() throws Exception {
        List<User> users = userService.findAll();
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), users);
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<User>> getUserById(@PathVariable("id") Long id) throws Exception {
        User user = userService.findById(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), user);
    }

    @GetMapping("/read/username/{username}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<User>> getUserByUsername(@PathVariable("username") String username) throws RuntimeException {
        User user = userService.findByUsername(username);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), user);
    }

    @GetMapping("/read/fullname/{fullname}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<User>>> getUsersByFullName(@PathVariable("fullname") String fullName) throws Exception {
        List<User> users = userService.findByFullName(fullName);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), users);
    }

    @GetMapping("/read/email/{email}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<User>>> getUsersByEmail(@PathVariable("email") String email) throws Exception {
        List<User> users = userService.findByEmail(email);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), users);
    }

    @GetMapping("/read/birthday/{birthday}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<User>>> getUsersByBirthday(@PathVariable("birthday") LocalDate birthday) throws Exception {
        List<User> users = userService.findByBirthDay(birthday);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), users);
    }

    @PutMapping("/update")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<UserDTO>> update(@RequestBody UserDTO userDTO) throws Exception {
        userService.updateUser(userDTO);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_UPDATED, appConfig.messageSource(), userDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> delete(@PathVariable("id") Long id) throws Exception {
        User user = userService.findById(id);
        userService.deleteUser(user);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_DELETED, appConfig.messageSource(), "User with ID " + id + " has been deleted.");
    }
}
