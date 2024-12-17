package com.nguyenvannhat.library.controllers.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.books.BookService;
import com.nguyenvannhat.library.services.users.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_BOOK_READ')")
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks().stream().map(
                book -> new BookDTO(book.getTitle(), book.getAuthor(), book.getPages())
        ).collect(Collectors.toList());
    }


    @PostMapping("/insertBook")
    @PreAuthorize("hasAuthority('ROLE_BOOK_CREATE')")
    public CustomResponse<?> insertBook(@RequestBody BookDTO bookDTO) {
        try {
            bookService.insertBook(bookDTO);
            return new CustomResponse<>(HttpStatus.ACCEPTED, "Book created successfully!", bookDTO);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }


    @PostMapping("/insertMultiBooks")
    @PreAuthorize("hasAuthority('ROLE_BOOK_CREATE')")
    public CustomResponse<?> insertMultiBooks(@RequestBody List<BookDTO> bookDTOs) {
        try {
            for (BookDTO bookDTO : bookDTOs) {
                bookService.insertBook(bookDTO);
            }
            return new CustomResponse<>(HttpStatus.ACCEPTED, "Books created successfully!", bookDTOs);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }


    @PostMapping("/importExcel")
    @PreAuthorize("hasAuthority('ROLE_BOOK_CREATE')")
    public CustomResponse<?> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            bookService.insertBooks(file);
            return new CustomResponse<>(HttpStatus.ACCEPTED, "Books imported successfully!", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }


    @GetMapping("/exportExcel")
    @PreAuthorize("hasAuthority('ROLE_BOOK_READ')")
    public CustomResponse<?> exportExcel(@RequestBody List<BookDTO> bookDTOS) {
        try {
            bookService.exportBooksToExcel(bookDTOS);
            return new CustomResponse<>(HttpStatus.ACCEPTED, "Books exported successfully!", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_BOOK_UPDATE')")
    public CustomResponse<?> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        try {
            bookService.updateBook(id, bookDTO);
            return new CustomResponse<>(HttpStatus.ACCEPTED, "Book updated successfully!", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_BOOK_DELETE')")
    public CustomResponse<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBookByID(id);
            return new CustomResponse<>(HttpStatus.ACCEPTED, "Book deleted successfully!", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @DeleteMapping("")
    @PreAuthorize("hasAuthority('ROLE_BOOK_DELETE')")
    public CustomResponse<?> deleteBook(@RequestBody BookDTO bookDTO) {
        try {
            bookService.deleteBook(bookDTO);
            return new CustomResponse<>(HttpStatus.ACCEPTED, "Book deleted successfully!", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
