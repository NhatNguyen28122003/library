package com.nguyenvannhat.library.services.categories;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.dtos.requests.CategoryRequest;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.BookCategoryRepository;
import com.nguyenvannhat.library.repositories.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoriesRepository categoriesRepository;
    private final BookCategoryRepository BookCategoryRepository;
    private final ModelMapper modelMapper;
    private final BookCategoryRepository bookCategoryRepository;

    @Override
    public CategoryDTO create(CategoryRequest request) {
        Optional<Category> existingCate = categoriesRepository.findByCode(request.getCode());
        if (existingCate.isPresent()) {
            log.error("Category with code {} already exists", request.getCode());
            throw new ApplicationException(Constant.ERROR_CATEGORY_EXIST);
        }
        Category category = Category.builder()
                .code(request.getCode().toUpperCase())
                .name(request.getName())
                .build();
        category = categoriesRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoriesRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> findByCategory(Category category) {
        List<Book> books = bookCategoryRepository.findByCategoryId(category.getId());
        return books.stream().map(book -> modelMapper.map(book, BookDTO.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO update(Long id, CategoryRequest request) {
        Optional<Category> currentCate = categoriesRepository.findById(id);
        if (currentCate.isPresent()) {
            Category category = currentCate.get();
            Optional<Category> existingCate = categoriesRepository.findByCode(request.getCode());
            if (existingCate.isEmpty()) {
                category.setCode(request.getCode().toLowerCase());
            }

            if (!Objects.isNull(request.getName())) {
                category.setName(request.getName());
            }
            category = categoriesRepository.save(category);
            return modelMapper.map(category, CategoryDTO.class);
        }
        return modelMapper.map(currentCate.get(), CategoryDTO.class);
    }

    public List<CategoryDTO> importExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApplicationException("File is empty");
        }

        try (InputStream inputStream = file.getInputStream()) {
            log.info("Reading excel file {}", file.getOriginalFilename());

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("Category");
            if (sheet == null) {
                log.error("Sheet with name 'Category' not found");
                throw new ApplicationException("Sheet not found");
            }

            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Bỏ qua dòng đầu tiên nếu là tiêu đề
            List<Category> category = new ArrayList<>();
            List<Category> existingCategories = categoriesRepository.findAll();
            Set<String> existCode = existingCategories.stream()
                    .map(Category::getCode)
                    .collect(Collectors.toSet());

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                String code = Optional.ofNullable(row.getCell(0))
                        .map(cell -> cell.getStringCellValue())
                        .orElse(null);
                if (code == null || code.trim().isEmpty() || existCode.contains(code)) {
                    continue; // Bỏ qua dòng nếu mã trống hoặc đã tồn tại
                }

                String name = Optional.ofNullable(row.getCell(1))
                        .map(cell -> cell.getStringCellValue())
                        .orElse(null);
                if (name == null || name.trim().isEmpty()) {
                    continue; // Bỏ qua dòng nếu tên trống
                }

                category.add(Category.builder()
                        .code(code)
                        .name(name)
                        .build());
                existCode.add(code);
            }

            category = categoriesRepository.saveAll(category);
            return category.stream()
                    .map(c -> modelMapper.map(c, CategoryDTO.class))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Error reading the Excel file: {}", e.getMessage());
            throw new ApplicationException(Constant.ERROR_BOOK_NOT_FOUND);
        }
    }


    @Override
    public byte[] exportExcel() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Categories");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Code");
            row.createCell(1).setCellValue("Name");

            List<Category> categories = categoriesRepository.findAll();
            int i = 1;
            for (Category category : categories) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(category.getCode());
                row.createCell(1).setCellValue(category.getName());
                i++;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return out.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
