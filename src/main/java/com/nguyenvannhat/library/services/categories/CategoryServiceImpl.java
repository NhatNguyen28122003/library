package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.BookCategoryRepository;
import com.nguyenvannhat.library.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Override
    public Category addCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public List<CategoryDTO> addMultipleCategories(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        // Bỏ qua dòng đầu tiên
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            categoryRepository.save(Category.builder()
                    .name(row.getCell(0).getStringCellValue())
                    .build());
        }
        return getCategories();
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_CATEGORY_NOT_FOUND)
        );
        if (categoryRepository.findByName(categoryDTO.getName()).isEmpty()) {
            category.setName(categoryDTO.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findByName(categoryDTO.getName()).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_CATEGORY_NOT_FOUND)
        );
        bookCategoryRepository.deleteByCategoryId(category.getId());
        categoryRepository.deleteByName(categoryDTO.getName());
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_CATEGORY_NOT_FOUND)
        );
        bookCategoryRepository.deleteByCategoryId(category.getId());
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAll().stream().map(
                category -> new CategoryDTO(category.getName())
        ).toList();
    }

    @Override
    public List<Book> getBooksByCategory(Category category) {
        return categoryRepository.findBooksByCategory(category);
    }
}
