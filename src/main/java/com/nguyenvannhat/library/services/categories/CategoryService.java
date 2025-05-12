package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.dtos.requests.CategoryRequest;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryDTO create(CategoryRequest request);
    List<CategoryDTO> getAllCategories();
    List<BookDTO> findByCategory(Category category);
    CategoryDTO update(Long id, CategoryRequest request);
    List<CategoryDTO> importExcel(MultipartFile file);
    byte[] exportExcel();
}
