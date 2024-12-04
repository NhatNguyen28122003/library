package com.nguyenvannhat.library.controllers;

import com.nguyenvannhat.library.dtos.UserDTO;
import com.nguyenvannhat.library.models.User;
import com.nguyenvannhat.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/users/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        try {
            userService.register(userDTO);
            return ResponseEntity.ok("Create successfully!!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users/createMultiUser")
    public ResponseEntity<String> create(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                UserDTO userDTO = new UserDTO();
                userDTO.setName(row.getCell(0).getStringCellValue().trim());
                userDTO.setPhoneNumber(row.getCell(1).getStringCellValue().trim());
                userDTO.setPassword(row.getCell(2).getStringCellValue().trim());
                userDTO.setRole(row.getCell(3).getStringCellValue().trim());
                userService.register(userDTO);
            }
            return ResponseEntity.ok("Users created successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing the file");
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.login(userDTO.getPhoneNumber(), userDTO.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public List<UserDTO> users() {
        return  userService.getAllUsers().stream()
                .map(user -> new UserDTO(
                        user.getFullName(),
                        user.getPhoneNumber()
                ))
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("Delete successfully!!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
