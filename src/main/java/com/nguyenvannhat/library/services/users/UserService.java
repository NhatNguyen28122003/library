package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.respones.LoginResponse;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    void register(UserDTO userDTO) throws Exception;
    LoginResponse login(String email, String password) throws Exception;
    User updateUser(UserDTO userDTO) throws Exception;
    User findByUsername(String username) throws Exception;
    List<User> findByFullName(String fullName) throws Exception;
    List<User> findByEmail(String email) throws Exception;
    User findByPhoneNumber(String phoneNumber) throws Exception;
    List<User> findByBirthDay(LocalDate birthDay) throws Exception;
    void deleteUser(User user);
    List<GrantedAuthority> getAuthorities() throws Exception;
}
