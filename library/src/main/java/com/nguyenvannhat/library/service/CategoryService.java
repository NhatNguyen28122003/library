package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.models.Category;

import java.util.List;

public interface CategoryService {
    Category insertCategory(CategoryDTO categoryDTO);

    Category updateCategory(int id, CategoryDTO categoryDTO);

    List<Category> getAllCategories();

    Category getById(int id);

    Category getByName(String name);

    void deleteById(int id);

    void deleteByName(String name);
}
