package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.dtos.requests.CategoryRequest;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.services.categories.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryRequest category) {
        CategoryDTO categoryDTO = categoryService.create(category);
        return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
    }


    @GetMapping("/getAllCategory")
    @PreAuthorize("fileRole(#request)")
    public ResponseEntity<List<CategoryDTO>> getAllCategory(HttpServletRequest request) {
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategories();
        return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
    }

    @GetMapping("/getBooks")
    public ResponseEntity<List<BookDTO>> getBookByCategory(@RequestBody Category category) {
        List<BookDTO> bookDTOS = categoryService.findByCategory(category);
        return new ResponseEntity<>(bookDTOS, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@Param("id") Long id, @RequestBody CategoryRequest category) {
        CategoryDTO  categoryDTO = categoryService.update(id, category);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @PostMapping("/importExcel")
    public ResponseEntity<List<CategoryDTO>> importExcel(@RequestParam("file") MultipartFile file) {
        List<CategoryDTO> categoryDTOS = categoryService.importExcel(file);
        return new ResponseEntity<>(categoryDTOS, HttpStatus.OK);
    }

    @PostMapping("/exportExcel")
    public ResponseEntity<byte[]> exportExcel() {
        byte[] bytes = categoryService.exportExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "categories.xlsx");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
