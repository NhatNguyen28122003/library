package com.nguyenvannhat.library.controllers;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.models.Category;
import com.nguyenvannhat.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping("/categories/insertCategory")
    public ResponseEntity<String> insertCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.insertCategory(categoryDTO);
        return ResponseEntity.ok("Create category success!!!");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping("/categories/insertMultiCategories")
    public ResponseEntity<String> insertCategories(@RequestParam("file") MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String categoryName = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : null;
                if (categoryName != null && !categoryName.trim().isEmpty()) {
                    CategoryDTO categoryDTO = new CategoryDTO(categoryName);
                    categoryService.insertCategory(categoryDTO);
                } else {
                    return ResponseEntity.badRequest().body("Category name is missing in one of the rows.");
                }
            }
            return ResponseEntity.ok("Categories created successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File processing error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PutMapping("/categories/updateCategory/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable int id, @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Update category success!!!");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @DeleteMapping("/categories/updateCategory/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok(String.format("Delete category with id = %d success!!!", id));
    }
}
