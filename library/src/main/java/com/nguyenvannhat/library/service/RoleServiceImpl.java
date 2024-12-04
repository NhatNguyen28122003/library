package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.RoleDTO;
import com.nguyenvannhat.library.models.Role;
import com.nguyenvannhat.library.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    @Autowired
    private  RoleRepository roleRepository;

    @Override
    public Role createRole(RoleDTO roleDTO) throws Exception {
        if (roleRepository.findByName(roleDTO.getName()).isPresent()) {
            throw new RuntimeException("Role can be existed!!!");
        }
        Role newRole = Role.builder()
                .id(0)
                .name(roleDTO.getName())
                .build();
        roleRepository.save(newRole);
        return newRole;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll().stream().toList();
    }

    @Override
    public Role getRoleById(int id) {
        Optional<Role> existingRole = roleRepository.findById(id);
        if (existingRole.isEmpty()) {
            throw new RuntimeException("Not find role by id: " + id);
        }
        return existingRole.get();
    }

    @Override
    public Role updateRole(int id, RoleDTO roleDTO) {
        Optional<Role> existingRole = roleRepository.findById(id);
        if (existingRole.isEmpty()) {
            throw new RuntimeException("Not find role by id: " + id);
        }

        Role role = existingRole.get();
        role.setName(roleDTO.getName());
        roleRepository.save(role);
        return role;
    }

    @Override
    public void deleteRoleById(int id) {
        if (roleRepository.findById(id).isEmpty()) {
            return;
        }
        roleRepository.deleteById(id);
    }
}
