package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.BookCateRepository;
import com.nguyenvannhat.library.repositories.CategoriesRepository;
import com.nguyenvannhat.library.responses.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoriesRepository categoriesRepository;
    private final BookCateRepository bookCateRepository;
    private final MessageSource messageSource;
    private static final Locale locale = Locale.ENGLISH;

    @Override
    public CustomResponse<List<CategoryDTO>> createCategory(CategoryDTO categoryDTO) {
        if (categoriesRepository.findByName(categoryDTO.getName()).isPresent()) {
            throw new ApplicationException(Constant.ERROR_CATEGORY_EXIST);
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();
        categoriesRepository.save(category);
        List<CategoryDTO> categoryDTOS = categoriesRepository.findAll().stream().map(
                category1 -> new CategoryDTO(category1.getName())
        ).toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_CREATED, null, locale),
                categoryDTOS);
    }

    @Override
    public CustomResponse<List<CategoryDTO>> createMultiCategories(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        rowIterator.next();
        List<Category> categories = new ArrayList<>();
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            String name = row.getCell(0).getStringCellValue();
            if (categoriesRepository.findByName(name).isEmpty()) {
                categories.add(Category.builder()
                        .name(name)
                        .build());
            }
        }
        categoriesRepository.saveAll(categories);
        workbook.close();
        List<CategoryDTO> categoryDTOS = categoriesRepository.findAll().stream().map(
                category1 -> new CategoryDTO(category1.getName())
        ).toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_CREATED, null, locale),
                categoryDTOS);
    }

    @Override
    public CustomResponse<Category> findById(Long id) {
        Category category = categoriesRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_CATEGORY_NOT_FOUND)
        );
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_GET_INFORMATION, null, locale),
                category);
    }

    @Override
    public CustomResponse<Category> findByName(String categoryName) {
        Category category = categoriesRepository.findByName(categoryName).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_CATEGORY_NOT_FOUND)
        );
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_GET_INFORMATION, null, locale),
                category);
    }

    @Override
    public CustomResponse<List<CategoryDTO>> findAllCategories() {
        List<CategoryDTO> categoryDTOS = categoriesRepository.findAll().stream().map(
                category1 -> new CategoryDTO(category1.getName())
        ).toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_GET_INFORMATION, null, locale),
                categoryDTOS);
    }

    @Override
    public CustomResponse<List<CategoryDTO>> updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoriesRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_CATEGORY_NOT_FOUND)
        );
        if (categoriesRepository.findByName(categoryDTO.getName()).isEmpty()) {
            category.setName(categoryDTO.getName());
        }
        List<CategoryDTO> categoryDTOS = categoriesRepository.findAll().stream().map(
                category1 -> new CategoryDTO(category1.getName())
        ).toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_UPDATED, null, locale),
                categoryDTOS);
    }

    @Override
    public CustomResponse<List<CategoryDTO>> deleteCategory(String categoryName) {
        Category category = categoriesRepository.findByName(categoryName).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_CATEGORY_NOT_FOUND)
        );
        categoriesRepository.delete(category);
        List<CategoryDTO> categoryDTOS = categoriesRepository.findAll().stream().map(
                category1 -> new CategoryDTO(category1.getName())
        ).toList();
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_DELETED, null, locale),
                categoryDTOS);
    }

    @Override
    public CustomResponse<List<Book>> findByCategory(String categoryName) {
        Category category = categoriesRepository.findByName(categoryName).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_CATEGORY_NOT_FOUND)
        );
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_CATEGORY_GET_INFORMATION, null, locale),
                bookCateRepository.findBooksByCategory(category));
    }
}
