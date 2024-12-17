package com.nguyenvannhat.library.controllers.categories;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.repositories.CategoryRepository;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.categories.CategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoriesService categoriesService;

    @GetMapping("/read")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return CustomResponse.success(categories);
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).get();
        return CustomResponse.success(category);
    }

    @PostMapping("/insert")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> insertCategory(@RequestBody CategoryDTO categoryDTO) throws Exception {
        categoriesService.createCategory(categoryDTO);
        return CustomResponse.success(categoriesService.getAllCategories());
    }

    @PutMapping("/update")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) throws Exception {
        categoriesService.updateCategory(id, categoryDTO);
        return CustomResponse.success(categoriesService.getAllCategories());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return CustomResponse.success(categoriesService.getAllCategories());
    }
}
