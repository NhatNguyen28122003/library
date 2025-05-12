package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.components.JwtUtils;
import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.LoginDTO;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.dtos.requests.UserRequest;
import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.entities.UserRole;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.RoleRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import com.nguyenvannhat.library.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO register(UserRequest userRequest) {
        String userName = userRequest.getUserName();
        Optional<User> existingUser = userRepository.findByUserName(userName);
        if (existingUser.isPresent()) {
            log.error("User already exists");
            throw new ApplicationException(Constant.ERROR_USER_EXIST);
        }
        User newUser = User.builder()
                .userName(userName)
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .email(userRequest.getEmail())
                .birthDay(userRequest.getBirthDay())
                .age(userRequest.getAge())
                .fullName(userRequest.getFullName())
                .isBorrowed(true)
                .identityNumber(userRequest.getIdentityNumber())
                .build();
        newUser.setCreateBy(newUser.getUserName());
        newUser = userRepository.save(newUser);
        Optional<Role> roleUser = roleRepository.findByCode("user");
        if (roleUser.isPresent()) {
            Role role = roleUser.get();
            UserRole userRole = UserRole.builder()
                    .userId(newUser.getId())
                    .roleId(role.getId())
                    .build();
            userRoleRepository.save(userRole);
        }
        return modelMapper.map(newUser, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
    }

    @Override
    public LoginDTO login(String userName, String password) {
        log.info("----Bắt đầu đăng nhập ----");
        Optional<User> existingUser = userRepository.findByUserName(userName);
        if (existingUser.isEmpty()) {
            log.error("User doesn't exist");
            throw new ApplicationException(Constant.ERROR_WRONG_USER_NAME_PASSWORD);
        }
        User user = existingUser.get();
        if (user.getIsDeleted().equals(Boolean.TRUE)) {
            log.error("Tài khoản đã bị xóa");
            throw new ApplicationException(Constant.ERROR_WRONG_USER_NAME_PASSWORD);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("Password nhập không đúng");
            throw new ApplicationException(Constant.ERROR_WRONG_USER_NAME_PASSWORD);
        }
        String token = jwtUtils.generateToken(user);
        OffsetDateTime expiration = jwtUtils.getExpirationDateFromToken(token);
        log.info("---Hoàn tất dăng nhập---");
        return LoginDTO.builder()
                .userName(user.getUserName())
                .token(token)
                .expiration(expiration)
                .build();
    }

    @Override
    public UserDTO update(Long id, UserRequest userRequest) {
        return null;
    }

    @Override
    public UserDTO softDelete(User user) {
        user.setIsDeleted(true);
        userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void deleteAccount(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
    }
}
