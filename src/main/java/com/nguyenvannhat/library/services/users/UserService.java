package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.respones.LoginResponse;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(UserDTO userDTO) throws Exception;
    LoginResponse login(String email, String password) throws Exception;
    User updateUser(UserDTO userDTO) throws Exception;
    User findByUsername(String username) throws Exception;
    User findByFullName(String fullName) throws Exception;
    User findByEmail(String email) throws Exception;
    User findByPhoneNumber(String phoneNumber) throws Exception;
    User findByBirthDay(LocalDate birthDay) throws Exception;
    void deleteUser(User user) throws Exception;
    List<GrantedAuthority> getAuthorities() throws Exception;
}
