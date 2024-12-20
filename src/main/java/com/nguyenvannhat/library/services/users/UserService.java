package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.UserCustom;
import com.nguyenvannhat.library.responses.LoginResponse;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    void register(UserDTO userDTO) ;
    LoginResponse login(String email, String password) ;
    void updateUser(UserDTO userDTO) ;
    List<UserCustom> findAll();
    UserCustom findByUsername(String username);
    UserCustom findById(Long id);
    List<UserCustom> findByFullName(String fullName);
    List<UserCustom> findByEmail(String email);
    UserCustom findByPhoneNumber(String phoneNumber);
    List<UserCustom> findByBirthDay(LocalDate birthDay);
    void deleteUser(UserCustom userCustom);
    List<GrantedAuthority> getAuthorities();
}
