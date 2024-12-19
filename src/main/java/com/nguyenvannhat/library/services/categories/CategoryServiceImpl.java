package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.exceptions.ErrorCode;
import com.nguyenvannhat.library.repositories.BookCategoryRepository;
import com.nguyenvannhat.library.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoriesService{
    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;
    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(
                category -> new CategoryDTO(category.getName()
                )).collect(Collectors.toList());
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND)
        );
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(
                () -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND)
        );
    }

    @Override
    public void createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new ApplicationException(ErrorCode.CATEGORY_EXIST);
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        if (categoryRepository.findById(id).isPresent() &&
        categoryRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new ApplicationException(ErrorCode.CATEGORY_EXIST);
        }
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND)
        );
        category.setName(categoryDTO.getName());
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND)
        );
        bookCategoryRepository.deleteByCategoryId(category.getId());
        categoryRepository.delete(category);
    }

    @Override
    public void deleteCategoryByName(String name) {
        Category category = categoryRepository.findByName(name).orElseThrow(
                () -> new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND)
        );
        bookCategoryRepository.deleteByCategoryId(category.getId());
        categoryRepository.delete(category);
    }
}
