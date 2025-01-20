package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.categories.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;


    @PreAuthorize("fileRole(#request)")
    @PostMapping("/create")
    public CustomResponse<List<CategoryDTO>> createCategory(HttpServletRequest request, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    @PreAuthorize("fileRole(#request)")
    @PostMapping("/create/multi")
    public CustomResponse<List<CategoryDTO>> createMultiCategory(HttpServletRequest request, @RequestParam MultipartFile multipartFile) throws IOException {
        return categoryService.createMultiCategories(multipartFile);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read")
    public CustomResponse<List<CategoryDTO>> findAllCategory(HttpServletRequest request) {
        return categoryService.findAllCategories();
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/{id}")
    public CustomResponse<Category> findById(HttpServletRequest request, @PathVariable("id") Long id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/{name}")
    public CustomResponse<Category> findByName(HttpServletRequest request, @PathVariable("name") String name) {
        return categoryService.findByName(name);
    }

    @PreAuthorize("fileRole(#request)")
    @PutMapping("/update/{id}")
    public CustomResponse<List<CategoryDTO>> createCategory(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    @PreAuthorize("fileRole(#request)")
    @DeleteMapping("/delete/{name}")
    public CustomResponse<List<CategoryDTO>> createCategory(HttpServletRequest request, @PathVariable("name") String name) {
        return categoryService.deleteCategory(name);
    }
}
