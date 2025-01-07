package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.entities.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User register(UserDTO userDTO);

    String login(UserDTO userDTO);

    User getUserByEmail(String email);

    User getUserById(Long id);

    User getUserByUsername(String username);

    User getUserByPhoneNumber(String phoneNumber);

    List<UserDTO> getAllUsers();

    List<UserDTO> getAllUsersByAddress(String address);

    List<UserDTO> getUsersByAge(Integer age);

    List<UserDTO> getUsersByBirthday(LocalDate birthday);

    User update(UserDTO userDTO);
}
