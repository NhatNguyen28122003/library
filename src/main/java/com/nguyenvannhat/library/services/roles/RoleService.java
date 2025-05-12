package com.nguyenvannhat.library.services.roles;

import com.nguyenvannhat.library.dtos.RoleDTO;
import com.nguyenvannhat.library.dtos.requests.RoleRequest;
import com.nguyenvannhat.library.entities.Role;

import java.util.List;

public interface RoleService {
    RoleDTO add(RoleRequest role);
    RoleDTO update(Long id, RoleRequest role);
    List<RoleDTO> getAllRole();
    List<RoleDTO> search(String keyword);
    RoleDTO getRole(Long id);
    void softDeleted(Long id);
    void deleteRole(Role role);
}
