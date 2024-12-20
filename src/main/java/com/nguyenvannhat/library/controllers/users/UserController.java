//package com.nguyenvannhat.library.controllers.users;
//
//import com.nguyenvannhat.library.dtos.UserDTO;
//import com.nguyenvannhat.library.entities.User;
//import com.nguyenvannhat.library.responses.CustomResponse;
//import com.nguyenvannhat.library.responses.LoginResponse;
//import com.nguyenvannhat.library.services.users.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/user")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @PostMapping("/register")
//    public  ResponseEntity<?> register(@RequestBody UserDTO userDTO) throws Exception {
//        userService.register(userDTO);
//        return CustomResponse.success(HttpStatus.CREATED,userDTO);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody UserDTO userDTO) throws Exception {
//        LoginResponse loginResponse = userService.login(userDTO.getUsername(), userDTO.getPassword());
//        return new ResponseEntity<>(loginResponse,HttpStatus.OK);
//    }
//
//    @GetMapping("/information")
//    public ResponseEntity<?> getUser() throws Exception {
//        List<GrantedAuthority> grantedAuthorities = userService.getAuthorities();
//        return CustomResponse.success(HttpStatus.OK,grantedAuthorities);
//    }
//
//    @GetMapping("/read/{id}")
//    @PreAuthorize("fileRole()")
//    public ResponseEntity<?> getUser(@PathVariable("id") Long id) throws Exception {
//        User user = userService.findById(id);
//        return CustomResponse.success(HttpStatus.OK,user);
//    }
//    @PutMapping("/update")
//    public ResponseEntity<?> update(@RequestBody UserDTO userDTO) throws Exception {
//        userService.updateUser(userDTO);
//        return CustomResponse.success(HttpStatus.OK,userDTO);
//    }
//
//
//}
