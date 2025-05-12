package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.dtos.requests.BookRequest;
import com.nguyenvannhat.library.services.books.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @PostMapping("/importExcel")
    public ResponseEntity<List<BookDTO>> importExcel(@RequestParam("file") MultipartFile file) {
        List<BookDTO> books = bookService.importExcel(file);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/add")
    public ResponseEntity<BookDTO> add(@RequestBody BookRequest book) {
        BookDTO bookDTO = bookService.addBook(book);
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping("/getAllBook")
    public ResponseEntity<List<BookDTO>> getAllBook() {
        List<BookDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookRequest book) {
        BookDTO bookDTO = bookService.update(id, book);
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        BookDTO bookDTO = bookService.getBook(id);
        return ResponseEntity.ok(bookDTO);
    }

    @PostMapping("/exportExcel")
    public ResponseEntity<byte[]> exportExcel() {
        byte[] bytes = bookService.exportExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "books.xlsx");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
