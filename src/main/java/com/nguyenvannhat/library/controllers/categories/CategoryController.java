package com.nguyenvannhat.library.controllers.categories;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.repositories.CategoryRepository;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.categories.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "API for managing book categories") // Gắn nhãn cho controller
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoriesService categoriesService;

    @GetMapping("/read")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get all categories", description = "Retrieve a list of all available categories.")
    public ResponseEntity<?> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return CustomResponse.success(categories);
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID.")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return CustomResponse.success(category);
    }

    @PostMapping("/insert")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Insert a new category", description = "Create a new category with the provided information.")
    public ResponseEntity<?> insertCategory(@RequestBody CategoryDTO categoryDTO) throws Exception {
        categoriesService.createCategory(categoryDTO);
        return CustomResponse.success(categoriesService.getAllCategories());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Update a category", description = "Update an existing category based on its ID.")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) throws Exception {
        categoriesService.updateCategory(id, categoryDTO);
        return CustomResponse.success(categoriesService.getAllCategories());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Delete a category", description = "Delete a category by its ID.")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return CustomResponse.success(categoriesService.getAllCategories());
    }
}
