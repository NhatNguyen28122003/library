package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.models.Category;
import com.nguyenvannhat.library.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public Category insertCategory(CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.getName());
        if (existingCategory.isPresent()) {
            throw new RuntimeException("Category's existed!!!");
        }
        Category category = Category.builder()
                .id(0)
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(int id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            throw new RuntimeException("Category's does not exist!!!");
        }
        Category category = existingCategory.get();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Not find category by id " + 2));
    }

    @Override
    public Category getByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new RuntimeException("Not find category by name " + name));
    }

    @Override
    public void deleteById(int id) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isEmpty()) {
            throw new RuntimeException("Category's does not exist!!!");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isEmpty()) {
            throw new RuntimeException("Category's does not exist!!!");
        }
        int id = existingCategory.get().getId();
        categoryRepository.deleteById(id);
    }
}
