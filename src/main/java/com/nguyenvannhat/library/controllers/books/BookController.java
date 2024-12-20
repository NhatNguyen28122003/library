package com.nguyenvannhat.library.controllers.books;

import com.nguyenvannhat.library.components.AppConfig;
import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.responses.SuccessCode;
import com.nguyenvannhat.library.services.books.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AppConfig appConfig;

    @GetMapping
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<Book>>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return CustomResponse.success(HttpStatus.OK, SuccessCode.BOOK_INFORMATION, appConfig.messageSource(), books);
    }

    @PostMapping("/create")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<BookDTO>> createBook(@RequestBody BookDTO bookDTO) {
        bookService.insertBook(bookDTO);
        return CustomResponse.success(HttpStatus.CREATED, SuccessCode.BOOK_CREATED, appConfig.messageSource(), bookDTO);
    }

    @PostMapping("/create/bulk")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> createBooksFromFile(@RequestParam("file") MultipartFile file) throws IOException{
        bookService.insertBooks(file);
        return CustomResponse.success(HttpStatus.CREATED, SuccessCode.BOOK_CREATED, appConfig.messageSource(), "Books created successfully from file.");
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<BookDTO>> getBookById(@PathVariable("id") Long id) {
        BookDTO bookDTO = bookService.getBookById(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.BOOK_INFORMATION, appConfig.messageSource(), bookDTO);
    }

    @GetMapping("/read/category")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<List<BookDTO>>> getBooksByCategory(@RequestBody Category category) {
        List<BookDTO> books = bookService.findAllBooksByCategory(category);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.BOOK_INFORMATION, appConfig.messageSource(), books);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<BookDTO>> updateBook(@PathVariable("id") Long id, @RequestBody BookDTO bookDTO) {
        bookService.updateBook(id, bookDTO);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.BOOK_UPDATED, appConfig.messageSource(), bookDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteBookByID(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.BOOK_DELETED, appConfig.messageSource(), "Book with ID " + id + " has been deleted.");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> deleteBook(@RequestBody BookDTO bookDTO) {
        bookService.deleteBook(bookDTO);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.BOOK_DELETED, appConfig.messageSource(), "Book has been deleted.");
    }

    @GetMapping("/update/export")
    @PreAuthorize("fileRole()")
    public ResponseEntity<ByteArrayResource> exportBooksToExcel(@RequestBody List<BookDTO> bookDTOs) throws IOException {
        File file = bookService.exportBooksToExcel(bookDTOs);
        return CustomResponse.download(file.getAbsolutePath());
    }
}
