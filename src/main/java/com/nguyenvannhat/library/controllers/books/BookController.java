package com.nguyenvannhat.library.controllers.books;
import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.bookloan.BookLoanService;
import com.nguyenvannhat.library.services.books.BookService;
import com.nguyenvannhat.library.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Tag(name = "Book Management", description = "API for managing books") // Gắn nhãn cho toàn bộ controller
public class BookController {

    private final BookService bookService;
    private final BookLoanService bookLoanService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get all books", description = "Retrieve a list of all available books.")
    public ResponseEntity<?> getAllBooks() {
        return CustomResponse.success(bookService.getAllBooks());
    }

    @PostMapping("/insertBook")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Insert a new book", description = "Add a single book to the system.")
    public ResponseEntity<?> insertBook(@RequestBody BookDTO bookDTO) throws Exception {
        bookService.insertBook(bookDTO);
        return CustomResponse.success(bookService.getAllBooks());
    }

    @PostMapping("/insertMultiBooks")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Insert multiple books", description = "Add multiple books to the system at once.")
    public ResponseEntity<?> insertMultiBooks(@RequestBody List<BookDTO> bookDTOs) throws Exception {
        for (BookDTO bookDTO : bookDTOs) {
            bookService.insertBook(bookDTO);
        }
        return CustomResponse.success(bookService.getAllBooks());
    }

    @PostMapping("/insert/importExcel")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Import books from Excel file", description = "Import a list of books by uploading an Excel file.")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        bookService.insertBooks(file);
        return CustomResponse.success(bookService.getAllBooks());
    }

    @GetMapping("/read/exportExcel")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Export books to Excel", description = "Export a list of books to an Excel file for download.")
    public ResponseEntity<?> exportExcel(@RequestBody List<BookDTO> bookDTOS) throws Exception {
        File file = bookService.exportBooksToExcel(bookDTOS);
        return CustomResponse.download(file);
    }

    @GetMapping("/read/booksByUser")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get books borrowed by user", description = "Retrieve a list of books borrowed by the current authenticated user.")
    public ResponseEntity<?> getListBooksByUser() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        List<BookDTO> bookDTOS = bookLoanService.getListBooksByUser(user);
        return CustomResponse.success(bookDTOS);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Update book details", description = "Update the details of a book by its ID.")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) throws Exception {
        bookService.updateBook(id, bookDTO);
        return CustomResponse.success(bookService.getAllBooks());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Delete book by ID", description = "Delete a book from the system using its ID.")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBookByID(id);
        return CustomResponse.success(bookService.getAllBooks());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Delete book by title", description = "Delete a book using its title.")
    public ResponseEntity<?> deleteBook(@RequestBody BookDTO bookDTO) throws Exception {
        bookService.deleteBook(bookDTO);
        return CustomResponse.success(bookService.getAllBooks());
    }
}
