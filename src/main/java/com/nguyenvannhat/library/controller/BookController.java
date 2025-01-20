package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.books.BookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read")
    public CustomResponse<List<Book>> getBooks(HttpServletRequest request) {
        return bookService.findAllBook();
    }

    @PostMapping("/create/multi")
    public CustomResponse<List<Book>> createBooks(@RequestParam MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        return bookService.addMultiBook(multipartFile);
    }
}
