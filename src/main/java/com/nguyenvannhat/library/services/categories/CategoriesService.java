package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Category;

import java.util.List;

public interface CategoriesService {
    List<CategoryDTO> getAllCategories();
    Category getCategoryById(Long id);
    Category findByName(String name);
    void createCategory(CategoryDTO categoryDTO);
    void updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    void deleteCategoryByName(String name);

}
