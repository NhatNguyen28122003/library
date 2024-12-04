package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User register(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password);

    List<User> getAllUsers();

    Page<User> getListUser(int page, int limit);

    User getUserById(int id);

    User updateUser(int id, UserDTO userDTO);
    void deleteUserById(int id);

}
