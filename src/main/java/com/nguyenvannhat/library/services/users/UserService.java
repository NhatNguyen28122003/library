package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.dtos.LoginDTO;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.dtos.requests.UserRequest;
import com.nguyenvannhat.library.entities.User;

import java.util.List;

public interface UserService {
    UserDTO register(UserRequest userRequest);
    LoginDTO login(String userName, String password);
    List<UserDTO> getAllUsers();
    UserDTO update(Long id, UserRequest userRequest);
    UserDTO softDelete(User user);
    void deleteAccount(Long id);
}
