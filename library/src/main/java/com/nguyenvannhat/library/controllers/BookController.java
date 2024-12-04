package com.nguyenvannhat.library.controllers;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.models.Book;
import com.nguyenvannhat.library.models.Category;
import com.nguyenvannhat.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PostMapping("/books/insertBook")
    public ResponseEntity<String> insertCategory(@RequestBody BookDTO bookDTO) {
        bookService.insertBook(bookDTO);
        return ResponseEntity.ok("Insert book success!!!");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @PutMapping("/books/updateBook/{id}")
    public ResponseEntity<String> updateBook(@PathVariable int id, @RequestBody BookDTO bookDTO) {
        bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok("Update book success!!!");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @DeleteMapping("/books/updateBook/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        bookService.deleteById(id);
        return ResponseEntity.ok(String.format("Delete book with id = %d success!!!", id));
    }
}
