package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.components.JwtUtils;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.UserCustom;
import com.nguyenvannhat.library.entities.UserRole;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.exceptions.ErrorCode;
import com.nguyenvannhat.library.repositories.RoleRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import com.nguyenvannhat.library.repositories.UserRoleRepository;
import com.nguyenvannhat.library.responses.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserDTO userDTO){
        if (userDTO == null) {
            throw new ApplicationException(ErrorCode.WRONG_USER_NAME_PASSWORD);
        }
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ApplicationException(ErrorCode.USER_EXIST);
        }
        UserCustom userCustom = UserCustom.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword())) // Mã hóa mật khẩu
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .identityNumber(userDTO.getIdentityNumber() > 0 ? userDTO.getIdentityNumber() : null)
                .age(userDTO.getAge() > 0 ? userDTO.getAge() : null)
                .address(userDTO.getAddress())
                .build();
        userCustom.setCreateBy(userDTO.getUsername());
        userCustom.setUpdateBy(userDTO.getUsername());
        UserCustom userCustom1 = userRepository.save(userCustom);
        Role role = roleRepository.findByName("USER").orElseThrow(
                () -> new ApplicationException(ErrorCode.ROLE_NOT_FOUND)
        );
        UserRole userRole = new UserRole();
        userRole.setUserId(userCustom1.getId());
        userRole.setRoleId(role.getId());
        userRoleRepository.save(userRole);
    }


    @Override
    public LoginResponse login(String userName, String password) throws RuntimeException {
        UserCustom userCustom = userRepository.findByUsername(userName).orElseThrow(
                () -> new ApplicationException(ErrorCode.WRONG_USER_NAME_PASSWORD)
        );
        if (!passwordEncoder.matches(password, userCustom.getPassword())) {
            throw new ApplicationException(ErrorCode.WRONG_USER_NAME_PASSWORD);
        }
        String token = jwtUtils.generateToken(userCustom);
       return new LoginResponse()
                .setToken(token)
                .setExpires(jwtUtils.getExpirationDateFromToken(token));
    }

    @Override
    public UserCustom findById(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.USER_NOT_FOUND)
        );
    }

    @Override
    public void updateUser(UserDTO userDTO) throws RuntimeException {
        UserCustom userCustom = userRepository.findByUsername(userDTO.getUsername()).orElseThrow(
                () -> new ApplicationException(ErrorCode.USER_NOT_FOUND)
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userDTO.getUsername() != null &&
                userRepository.findByUsername(userDTO.getUsername()).isEmpty()) {
            userCustom.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPassword() != null) {
            userCustom.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getFullName() != null) {
            userCustom.setFullName(userDTO.getFullName());
        }
        if (!userDTO.getEmail().isEmpty()) {
            userCustom.setEmail(userDTO.getEmail());
        }

        if (!userDTO.getPhoneNumber().isEmpty()) {
            userCustom.setPhoneNumber(userDTO.getPhoneNumber());
        }

        if (!userDTO.getAddress().isEmpty()) {
            userCustom.setAddress(userDTO.getAddress());
        }
        userCustom.setUpdateBy(authentication.getName());
        userRepository.save(userCustom);
    }

    @Override
    public List<UserCustom> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserCustom findByUsername(String username) throws RuntimeException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ApplicationException(ErrorCode.USER_NOT_FOUND)
        );
    }

    @Override
    public List<UserCustom> findByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    @Override
    public List<UserCustom> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserCustom findByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new ApplicationException(ErrorCode.USER_NOT_FOUND)
        );
    }

    @Override
    public List<UserCustom> findByBirthDay(LocalDate birthDay) {
        return userRepository.findByBirthDay(birthDay);
    }

    @Override
    public void deleteUser(UserCustom userCustom) {
        if (userRepository.findByUsername(userCustom.getUsername()).isPresent()){
            userRepository.delete(userCustom);
        }
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ArrayList<>(authentication.getAuthorities());
    }

}
