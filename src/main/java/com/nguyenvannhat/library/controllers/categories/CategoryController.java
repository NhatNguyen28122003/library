package com.nguyenvannhat.library.controllers.categories;

import com.nguyenvannhat.library.components.AppConfig;
import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.responses.SuccessCode;
import com.nguyenvannhat.library.services.categories.CategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoriesService categoriesService;
    private final AppConfig appConfig;

    @GetMapping("/read")
    public ResponseEntity<CustomResponse<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoriesService.getAllCategories();
        return CustomResponse.success(HttpStatus.OK, SuccessCode.CATEGORY_INFORMATION, appConfig.messageSource(), categories);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<CustomResponse<Category>> getCategoryById(@PathVariable("id") Long id) throws Exception {
        Category category = categoriesService.getCategoryById(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.CATEGORY_INFORMATION, appConfig.messageSource(), category);
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<CustomResponse<Category>> getCategoryByName(@PathVariable("name") String name) throws Exception {
        Category category = categoriesService.findByName(name);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.CATEGORY_INFORMATION, appConfig.messageSource(), category);
    }

    @PostMapping("/create")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<CategoryDTO>> createCategory(@RequestBody CategoryDTO categoryDTO) throws Exception {
        categoriesService.createCategory(categoryDTO);
        return CustomResponse.success(HttpStatus.CREATED, SuccessCode.CATEGORY_CREATED, appConfig.messageSource(), categoryDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<CategoryDTO>> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryDTO categoryDTO) throws Exception {
        categoriesService.updateCategory(id, categoryDTO);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.CATEGORY_UPDATED, appConfig.messageSource(), categoryDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> deleteCategory(@PathVariable("id") Long id) throws Exception {
        categoriesService.deleteCategory(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.CATEGORY_DELETED, appConfig.messageSource(), "Category with ID " + id + " has been deleted.");
    }

    @DeleteMapping("/delete/name/{name}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> deleteCategoryByName(@PathVariable("name") String name) throws Exception {
        categoriesService.deleteCategoryByName(name);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.CATEGORY_DELETED, appConfig.messageSource(), "Category with name '" + name + "' has been deleted.");
    }
}
