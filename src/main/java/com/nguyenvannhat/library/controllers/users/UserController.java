package com.nguyenvannhat.library.controllers.users;

import com.nguyenvannhat.library.components.AppConfig;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.UserCustom;
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
    public ResponseEntity<CustomResponse<UserDTO>> register(@RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return CustomResponse.success(HttpStatus.CREATED, SuccessCode.USER_CREATED, appConfig.messageSource(), userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserDTO userDTO) {
        LoginResponse loginResponse = userService.login(userDTO.getUsername(), userDTO.getPassword());
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/information")
    public ResponseEntity<CustomResponse<List<GrantedAuthority>>> getUser() {
        List<GrantedAuthority> grantedAuthorities = userService.getAuthorities();
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), grantedAuthorities);
    }

    @GetMapping("/read")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<UserCustom>>> readAllUsers() {
        List<UserCustom> userCustoms = userService.findAll();
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), userCustoms);
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<UserCustom>> getUserById(@PathVariable("id") Long id) {
        UserCustom userCustom = userService.findById(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), userCustom);
    }

    @GetMapping("/read/username/{username}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<UserCustom>> getUserByUsername(@PathVariable("username") String username) {
        UserCustom userCustom = userService.findByUsername(username);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), userCustom);
    }

    @GetMapping("/read/fullname/{fullname}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<UserCustom>>> getUsersByFullName(@PathVariable("fullname") String fullName) {
        List<UserCustom> userCustoms = userService.findByFullName(fullName);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), userCustoms);
    }

    @GetMapping("/read/email/{email}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<UserCustom>>> getUsersByEmail(@PathVariable("email") String email) {
        List<UserCustom> userCustoms = userService.findByEmail(email);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), userCustoms);
    }

    @GetMapping("/read/birthday/{birthday}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<UserCustom>>> getUsersByBirthday(@PathVariable("birthday") LocalDate birthday) {
        List<UserCustom> userCustoms = userService.findByBirthDay(birthday);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_INFORMATION, appConfig.messageSource(), userCustoms);
    }

    @PutMapping("/update")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<UserDTO>> update(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_UPDATED, appConfig.messageSource(), userDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> delete(@PathVariable("id") Long id) {
        UserCustom userCustom = userService.findById(id);
        userService.deleteUser(userCustom);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.USER_DELETED, appConfig.messageSource(), "User with ID " + id + " has been deleted.");
    }
}
