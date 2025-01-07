package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    Category addCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> addMultipleCategories(MultipartFile file) throws IOException;

    Category updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(CategoryDTO categoryDTO);

    void deleteById(Long id);

    List<CategoryDTO> getCategories();

    List<Book> getBooksByCategory(Category category);
}
