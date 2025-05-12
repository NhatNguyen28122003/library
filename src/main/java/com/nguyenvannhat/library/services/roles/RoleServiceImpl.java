package com.nguyenvannhat.library.services.roles;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.RoleDTO;
import com.nguyenvannhat.library.dtos.requests.RoleRequest;
import com.nguyenvannhat.library.entities.Function;
import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.RoleFunction;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.FunctionRepository;
import com.nguyenvannhat.library.repositories.RoleFunctionRepository;
import com.nguyenvannhat.library.repositories.RoleRepository;
import com.nguyenvannhat.library.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleFunctionRepository roleFunctionRepository;
    private final ModelMapper modelMapper;
    private final UserRoleRepository userRoleRepository;
    private final FunctionRepository functionRepository;

    @Override
    public RoleDTO add(RoleRequest roleRequest) {
        Optional<Role> existingRole = roleRepository.findByCode(roleRequest.getCode().toLowerCase());
        if (existingRole.isPresent()) {
            log.error("Role with code {} already exists", roleRequest.getCode());
            throw new ApplicationException(Constant.ERROR_ROLE_NOT_FOUND);
        }

        Role newRole = Role.builder().code(roleRequest.getCode().toLowerCase()).name(roleRequest.getName()).description(roleRequest.getDescription()).build();
        newRole = roleRepository.save(newRole);
        if (roleRequest.getCode().equalsIgnoreCase("admin")) {
            addFunctionToRoleAdmin(newRole.getId());
        }
        return modelMapper.map(newRole, RoleDTO.class);
    }

    @Override
    public RoleDTO update(Long id, RoleRequest role) {
        Role currentRole = roleRepository.findById(id).orElseThrow(() -> {
            log.error("Role with id {} not found", id);
            throw new ApplicationException(Constant.ERROR_ROLE_NOT_FOUND);
        });

        Optional<Role> existingRole = roleRepository.findByCode(role.getCode().toLowerCase());
        if (existingRole.isEmpty()) {
            currentRole.setCode(role.getCode().toLowerCase());
            currentRole.setName(role.getName());
            currentRole.setDescription(role.getDescription());
        }
        roleRepository.save(currentRole);
        return modelMapper.map(currentRole, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAllRole() {
        return roleRepository.findByIsDeleted().stream().map(role -> modelMapper.map(role, RoleDTO.class)).collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRole(Long id) {
        Optional<Role> existingRole = roleRepository.findById(id);
        if (existingRole.isPresent()) {
            Role role = existingRole.get();
            if (!role.getIsDeleted()) {
                return modelMapper.map(role, RoleDTO.class);
            }
        }
        throw new ApplicationException(Constant.ERROR_ROLE_NOT_FOUND);
    }

    @Override
    public void softDeleted(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            Role currentRole = role.get();
            currentRole.setIsDeleted(true);
            roleRepository.save(currentRole);
        }
    }

    @Override
    public void deleteRole(Role role) {
        userRoleRepository.deleteUserRoleByRoleId(role.getId());
        roleFunctionRepository.deleteRoleFunctionByRoleId(role.getId());
        roleRepository.delete(role);
    }

    private void addFunctionToRoleAdmin(Long roleId) {
        List<Function> functions = functionRepository.findAll();
        if (!functions.isEmpty()) {
            List<RoleFunction> roleFunctions = new ArrayList<>();
            for (Function function : functions) {
                roleFunctions.add(RoleFunction.builder().roleId(roleId).functionId(function.getId()).build());
            }
            roleFunctionRepository.saveAll(roleFunctions);
        }
    }

    @Override
    public List<RoleDTO> search(String keyword) {
        List<Role> roles = roleRepository.search(keyword);
        return roles.stream().map(role -> modelMapper.map(role, RoleDTO.class)).collect(Collectors.toList());
    }
}
