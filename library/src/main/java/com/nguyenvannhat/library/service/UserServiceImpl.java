package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.component.JwtUtils;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.models.Role;
import com.nguyenvannhat.library.models.User;
import com.nguyenvannhat.library.repositories.RoleRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public User register(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByPhoneNumber(userDTO.getPhoneNumber());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists!");
        }
        User newUser = User.builder()
                .fullName(userDTO.getName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        if (userDTO.getRole() == null) {
            Role role = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            newUser.setRoles(Set.of(role));
        } else {
            Role role = roleRepository.findByName(userDTO.getRole().toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            newUser.setRoles(Set.of(role));
        }
        userRepository.save(newUser);

        return newUser;
    }


    @Override
    public String login(String phoneNumber, String password) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtils.generateToken(user);
        user.setToken(token);
        userRepository.save(user);
        return token;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().toList();
    }

    @Override
    public Page<User> getListUser(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(int id) {
        Optional<User> user = Optional.ofNullable(userRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Not found user by id: " + id)
                ));
        User user1 = user.get();
        return user1;
    }

    @Override
    public User updateUser(int id, UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("Not find user by id: " + id);
        }

        User user = existingUser.get();
        user.setFullName(userDTO.getName());
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }
}
