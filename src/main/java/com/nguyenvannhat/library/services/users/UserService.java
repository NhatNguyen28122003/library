package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User register(UserDTO userDTO);

    String login(UserDTO userDTO);

    CustomResponse<User> findById(Long id);

    CustomResponse<User> findByUserName(String username);

    CustomResponse<User> findByPhoneNumber(String phoneNumber);

    CustomResponse<User> findByEmail(String email);

    CustomResponse<User> findByIdentity(String identity);

    CustomResponse<List<UserDTO>> findAllUsers();

    CustomResponse<List<UserDTO>> findAllUsersByAge(Integer age);

    CustomResponse<List<UserDTO>> findAllUsersByAddress(String address);

    CustomResponse<List<UserDTO>> findAllUsersByBirthday(LocalDate birthday);

    CustomResponse<List<UserDTO>> findAllUsersFullName(String fullName);

    CustomResponse<User> update(Long id, UserDTO userDTO);

    void delete(Long id);
}
