package com.nguyenvannhat.library.controllers;

import com.nguyenvannhat.library.dtos.RoleDTO;
import com.nguyenvannhat.library.models.Role;
import com.nguyenvannhat.library.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/roles/createRole")
    public ResponseEntity<String> create(@RequestBody RoleDTO roleDTO) {
        try {
            roleService.createRole(roleDTO);
            return ResponseEntity.ok("Create successfully!!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/roles/updateRole/{id}")
    public ResponseEntity<String> create(@PathVariable int id, @RequestBody RoleDTO roleDTO) {
        try {
            roleService.updateRole(id, roleDTO);
            return ResponseEntity.ok("Update successfully!!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<String> delete(@PathVariable int id, @RequestBody RoleDTO roleDTO) {
        try {
            roleService.deleteRoleById(id);
            return ResponseEntity.ok("Delete successfully!!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
