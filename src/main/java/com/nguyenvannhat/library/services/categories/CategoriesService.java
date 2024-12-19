package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Category;

import java.util.List;

public interface CategoriesService {
    List<CategoryDTO> getAllCategories();
    Category getCategoryById(Long id) throws Exception;
    Category findByName(String name) throws Exception;
    void createCategory(CategoryDTO categoryDTO) throws Exception;
    void updateCategory(Long id, CategoryDTO categoryDTO) throws Exception;
    void deleteCategory(Long id) throws Exception;
    void deleteCategoryByName(String name) throws Exception;

}
