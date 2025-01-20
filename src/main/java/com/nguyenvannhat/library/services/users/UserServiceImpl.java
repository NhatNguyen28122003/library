package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.components.JwtUtils;
import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.*;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.*;
import com.nguyenvannhat.library.responses.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final UserPostRepository userPostRepository;
    private final UserCommentRepository userCommentRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Override
    public User register(UserDTO userDTO) {
        if (userRepository.findByUserName(userDTO.getUserName()).isPresent()) {
            throw new ApplicationException(Constant.ERROR_USER_EXIST);
        }
        User newUser = User.builder()
                .userName(userDTO.getUserName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .birthDay(userDTO.getBirthDay())
                .age(userDTO.getAge())
                .address(userDTO.getAddress())
                .identityNumber(userDTO.getIdentityNumber())
                .build();
        User user = userRepository.save(newUser);

        Role role = roleRepository.findByName("USER").orElseThrow(
                () -> new ApplicationException(Constant.ERROR_ROLE_NOT_FOUND)
        );
        userRoleRepository.save(UserRole.builder()
                .userId(user.getId())
                .roleId(role.getId())
                .build());
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
    public CustomResponse<User> findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_NOT_FOUND)
        );
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                user);
    }

    @Override
    public CustomResponse<User> findByUserName(String username) {
        User user = userRepository.findByUserName(username).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_NOT_FOUND)
        );
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                user);
    }

    @Override
    public CustomResponse<User> findByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_NOT_FOUND)
        );
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                user);
    }

    @Override
    public CustomResponse<User> findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_NOT_FOUND)
        );
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                user);
    }

    @Override
    public CustomResponse<User> findByIdentity(String identity) {
        User user = userRepository.findByIdentityNumber(identity).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_NOT_FOUND)
        );
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                user);
    }

    @Override
    public CustomResponse<List<UserDTO>> findAllUsers() {
        List<UserDTO> users = userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                users);
    }

    @Override
    public CustomResponse<List<UserDTO>> findAllUsersFullName(String fullName) {
        List<UserDTO> users = userRepository.findByFullName(fullName).stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                users);
    }

    @Override
    public CustomResponse<List<UserDTO>> findAllUsersByAge(Integer age) {
        List<UserDTO> users = userRepository.findByAge(age).stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                users);
    }

    @Override
    public CustomResponse<List<UserDTO>> findAllUsersByAddress(String address) {
        List<UserDTO> users = userRepository.findByAddress(address).stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                users);
    }

    @Override
    public CustomResponse<List<UserDTO>> findAllUsersByBirthday(LocalDate birthday) {
        List<UserDTO> users = userRepository.findByBirthDay(birthday).stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                users);
    }

    @Override
    public CustomResponse<User> update(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_NOT_FOUND)
        );
        if (userRepository.findByUserName(userDTO.getUserName()).isEmpty()) {
            user.setUserName(userDTO.getUserName());
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isEmpty()) {
            user.setEmail(userDTO.getEmail());
        }
        if (userRepository.findByPhoneNumber(String.valueOf(userDTO.getPhoneNumber())).isEmpty()) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        user.setFullName(userDTO.getFullName());
        user.setAddress(userDTO.getAddress());
        user.setBirthDay(userDTO.getBirthDay());
        user.setAge(userDTO.getAge());
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_USER_GET_INFORMATION, null, Locale.ENGLISH),
                userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            return;
        }
        List<UserRole> userRoles = userRoleRepository.findByUserId(id);
        List<UserPost> userPosts = userPostRepository.findAllByUser(userRepository.findById(id).get());
        List<UserComment> userComments = userCommentRepository.findAllByUser(userRepository.findById(id).get());
        userCommentRepository.deleteAll(userComments);
        userPostRepository.deleteAll(userPosts);
        userRoleRepository.deleteAll(userRoles);
        userRepository.deleteById(id);
    }
}
