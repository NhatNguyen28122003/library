package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.RoleDTO;
import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.models.Role;
import com.nguyenvannhat.library.models.User;


import java.util.List;

public interface RoleService {
    Role createRole(RoleDTO roleDTO) throws Exception;

    List<Role> getAllRoles();

    Role getRoleById(int id);

    Role updateRole(int id, RoleDTO roleDTO);
    void deleteRoleById(int id);
}
