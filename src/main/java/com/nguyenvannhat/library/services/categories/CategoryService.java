package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.responses.CustomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    CustomResponse<List<CategoryDTO>> createCategory(CategoryDTO categoryDTO);
    CustomResponse<List<CategoryDTO>> createMultiCategories(MultipartFile multipartFile) throws IOException;

    CustomResponse<Category> findById(Long id);

    CustomResponse<Category> findByName(String categoryName);

    CustomResponse<List<CategoryDTO>> findAllCategories();

    CustomResponse<List<CategoryDTO>> updateCategory(Long id, CategoryDTO categoryDTO);

    CustomResponse<List<CategoryDTO>> deleteCategory(String categoryName);

    CustomResponse<List<Book>> findByCategory(String categoryName);

}
