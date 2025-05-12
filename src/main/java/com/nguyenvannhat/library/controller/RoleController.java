package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.dtos.RoleDTO;
import com.nguyenvannhat.library.dtos.requests.RoleRequest;
import com.nguyenvannhat.library.services.roles.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<RoleDTO> addRole(@RequestBody RoleRequest role) {
        RoleDTO roleDTO = roleService.add(role);
        return ResponseEntity.ok(roleDTO);
    }

    @GetMapping("/getAllRole")
    public ResponseEntity<List<RoleDTO>> getAllRole() {
        return ResponseEntity.ok(roleService.getAllRole());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<RoleDTO> updateRole(@Param("id") Long id, @RequestBody RoleRequest role) {
        return ResponseEntity.ok(roleService.update(id,role));
    }

}
