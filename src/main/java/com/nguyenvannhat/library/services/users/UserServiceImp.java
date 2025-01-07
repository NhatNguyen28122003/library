package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.components.JwtUtils;
import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.entities.UserRole;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.RoleRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import com.nguyenvannhat.library.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public User register(UserDTO userDTO) {
        if (userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
            throw new ApplicationException(Constant.ERROR_USER_EXIST);
        }
        User user = User.builder()
                .userName(userDTO.getUserName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .birthDay(userDTO.getBirthDay())
                .identityNumber(userDTO.getIdentityNumber())
                .age(userDTO.getAge())
                .build();
        Long userId = userRepository.save(user).getId();
        Role role = roleRepository.findByName("USER").orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(role.getId())
                .build();
        userRoleRepository.save(userRole);

        return user;
    }

    @Override
    public String login(UserDTO userDTO) {
        User user = userRepository.findByUserName(userDTO.getUserName()).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_WRONG_USER_NAME_PASSWORD)
        );
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new ApplicationException(Constant.ERROR_WRONG_USER_NAME_PASSWORD);
        }
        return jwtUtils.generateToken(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Invalid email!!!")
        );
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_EXIST)
        );
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_EXIST)
        );
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_EXIST)
        );
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(
                user -> new UserDTO(user.getUserName(),
                        user.getPassword(),
                        user.getFullName(),
                        user.getPhoneNumber(),
                        user.getEmail(),
                        user.getIdentityNumber(),
                        user.getBirthDay(),
                        user.getAge(),
                        user.getAddress()
                )).toList();
    }

    @Override
    public List<UserDTO> getAllUsersByAddress(String address) {
        return userRepository.findByAddress(address).stream().map(
                user -> new UserDTO(user.getUserName(),
                        user.getPassword(),
                        user.getFullName(),
                        user.getPhoneNumber(),
                        user.getEmail(),
                        user.getIdentityNumber(),
                        user.getBirthDay(),
                        user.getAge(),
                        user.getAddress()
                )).toList();
    }

    @Override
    public List<UserDTO> getUsersByAge(Integer age) {
        return userRepository.findByAge(age).stream().map(
                user -> new UserDTO(user.getUserName(),
                        user.getPassword(),
                        user.getFullName(),
                        user.getPhoneNumber(),
                        user.getEmail(),
                        user.getIdentityNumber(),
                        user.getBirthDay(),
                        user.getAge(),
                        user.getAddress()
                )).toList();
    }

    @Override
    public List<UserDTO> getUsersByBirthday(LocalDate birthday) {
        return userRepository.findByBirthDay(birthday).stream().map(
                user -> new UserDTO(user.getUserName(),
                        user.getPassword(),
                        user.getFullName(),
                        user.getPhoneNumber(),
                        user.getEmail(),
                        user.getIdentityNumber(),
                        user.getBirthDay(),
                        user.getAge(),
                        user.getAddress()
                )).toList();
    }

    @Override
    public User update(UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String currentUsername = currentUser.getUsername();

        User userToUpdate = userRepository.findByUserName(userDTO.getUserName()).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_NOT_FOUND)
        );

        if (isAdmin(currentUser)) {
            updateUserInfo(userToUpdate, userDTO);
        } else {
            if (!currentUsername.equals(userDTO.getUserName())) {
                throw new ApplicationException(Constant.ERROR_USER_EXIST);
            }
            updateUserInfo(userToUpdate, userDTO);
        }
        return userRepository.save(userToUpdate);
    }

    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    private void updateUserInfo(User userToUpdate, UserDTO userDTO) {
        if (userRepository.findByUserName(userDTO.getUserName()).isEmpty()) {
            userToUpdate.setUserName(userDTO.getUserName());
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isEmpty()) {
            userToUpdate.setEmail(userDTO.getEmail());
        }
        if (userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isEmpty()) {
            userToUpdate.setPhoneNumber(userDTO.getPhoneNumber());
        }
        userToUpdate.setAddress(userDTO.getAddress());
    }
}
