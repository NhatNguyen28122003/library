package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.components.JwtUtils;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.entities.UserRole;
import com.nguyenvannhat.library.exceptions.DataAlreadyException;
import com.nguyenvannhat.library.exceptions.DataNotFoundException;
import com.nguyenvannhat.library.exceptions.InvalidDataException;
import com.nguyenvannhat.library.repositories.RoleRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import com.nguyenvannhat.library.repositories.UserRoleRepository;
import com.nguyenvannhat.library.respones.LoginResponse;
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
    public void register(UserDTO userDTO) throws Exception {
        if (userDTO == null) {
            throw new IllegalArgumentException("invalid Username/password");
        }
        userRepository.findByUsername(userDTO.getUsername())
                .ifPresent(user -> {
                    try {
                        throw new DataAlreadyException("User already exists");
                    } catch (DataAlreadyException e) {
                        throw new RuntimeException(e);
                    }
                });
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword())) // Mã hóa mật khẩu
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .identityNumber(userDTO.getIdentityNumber() > 0 ? userDTO.getIdentityNumber() : null)
                .age(userDTO.getAge() > 0 ? userDTO.getAge() : null)
                .address(userDTO.getAddress())
                .build();
        user.setCreateBy(userDTO.getUsername());
        user.setUpdateBy(userDTO.getUsername());
        User user1 = userRepository.save(user);
        Role role = roleRepository.findByName("USER").orElseThrow(() -> new DataNotFoundException("Role not found"));
        UserRole userRole = new UserRole();
        userRole.setUserId(user1.getId());
        userRole.setRoleId(role.getId());
        userRoleRepository.save(userRole);
    }


    @Override
    public LoginResponse login(String userName, String password) throws Exception {
        User user = userRepository.findByUsername(userName).orElseThrow(
                () -> new InvalidDataException("Invalid username or password")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidDataException("Invalid username/password!");
        }
        String token = jwtUtils.generateToken(user);
       return new LoginResponse()
                .setToken(token)
                .setExpires(jwtUtils.getExpirationDateFromToken(token));
    }

    @Override
    public User updateUser(UserDTO userDTO) throws Exception {
        User user = userRepository.findByUsername(userDTO.getUsername()).orElseThrow(
                () -> new DataNotFoundException("User not found")
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userDTO.getUsername() != null &&
                userRepository.findByUsername(userDTO.getUsername()).isEmpty()) {
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (!userDTO.getEmail().isEmpty()) {
            user.setEmail(userDTO.getEmail());
        }

        if (!userDTO.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }

        if (!userDTO.getAddress().isEmpty()) {
            user.setAddress(userDTO.getAddress());
        }
        user.setUpdateBy(authentication.getName());
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) throws Exception {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new DataNotFoundException("User not found!")
        );
    }

    @Override
    public List<User> findByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }

    @Override
    public List<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) throws Exception {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new DataNotFoundException("User not found!")
        );
    }

    @Override
    public List<User> findByBirthDay(LocalDate birthDay) {
        return userRepository.findByBirthDay(birthDay);
    }

    @Override
    public void deleteUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            userRepository.delete(user);
        }
    }

    @Override
    public List<GrantedAuthority> getAuthorities() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("No authenticated user found");
        }
        return new ArrayList<>(authentication.getAuthorities());
    }

}
