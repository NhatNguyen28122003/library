package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.exceptions.DataAlreadyException;
import com.nguyenvannhat.library.exceptions.DataNotFoundException;
import com.nguyenvannhat.library.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoriesService{
    private final CategoryRepository categoryRepository;
    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(
                category -> new CategoryDTO(category.getName()
                )).collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(Long id) throws Exception{
        return categoryRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Category not found!")
        );
    }

    @Override
    public Category findByName(String name) throws Exception {
        return categoryRepository.findByName(name).orElseThrow(
                () -> new DataNotFoundException("Category not found!")
        );
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) throws Exception {
        if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new DataAlreadyException("Category already exists!");
        }
        Category category = new Category();
        category.setId(0L);
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) throws Exception {
        if (categoryRepository.findById(id).isPresent() &&
        categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new DataAlreadyException("Category already exists!");
        }
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Category not found!")
        );
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) throws Exception {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Category not found!")
        );
        categoryRepository.delete(category);
    }

    @Override
    public void deleteCategoryByName(String name) throws Exception {
        Category category = categoryRepository.findByName(name).orElseThrow(
                () -> new DataNotFoundException("Category not found!")
        );
        categoryRepository.delete(category);
    }
}
