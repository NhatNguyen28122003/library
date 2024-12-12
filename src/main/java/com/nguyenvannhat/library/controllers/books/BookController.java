package com.nguyenvannhat.library.controllers.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.exceptions.DataNotFoundException;
import com.nguyenvannhat.library.respones.CustomResponse;
import com.nguyenvannhat.library.services.books.BookService;
import com.nguyenvannhat.library.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserService userService;

    @GetMapping("")
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks().stream().map(
                book -> new BookDTO(book.getTitle(), book.getAuthor(), book.getPages())
        ).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER') and hasRole('CREATE_BOOK')")
    @PostMapping("/insertBook")
    public CustomResponse<?> insertBook(@RequestBody BookDTO bookDTO) throws Exception {
        bookService.insertBook(bookDTO);
        return new CustomResponse<>(HttpStatus.ACCEPTED,"", bookDTO);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER') and hasRole('CREATE_BOOK')")
    @PostMapping("/insertMultiBooks")
    public CustomResponse<?> insertMultiBooks(@RequestBody List<BookDTO> bookDTOs) throws Exception {
        for (BookDTO bookDTO : bookDTOs) {
            bookService.insertBook(bookDTO);
        }
        return new CustomResponse<>(HttpStatus.ACCEPTED,"", bookDTOs);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER') and hasRole('CREATE_BOOK')")
    @PostMapping("/importExcel")
    public CustomResponse<?> importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            bookService.insertBooks(file);
            return new CustomResponse<>(HttpStatus.ACCEPTED,"", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "", e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER') and hasRole('CREATE_BOOK')")
    @GetMapping("/exportExcel")
    public CustomResponse<?> exportExcel(@RequestBody List<BookDTO> bookDTOS) throws Exception {
        try {
            bookService.exportBooksToExcel(bookDTOS);
            return new CustomResponse<>(HttpStatus.ACCEPTED,"", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "", e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER') and hasRole('CREATE_BOOK')")
    @PutMapping("/{id}")
    public CustomResponse<?> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) throws Exception {
        try {
            bookService.updateBook(id, bookDTO);
            return new CustomResponse<>(HttpStatus.ACCEPTED,"", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "", e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER') and hasRole('CREATE_BOOK')")
    @DeleteMapping("/{id}")
    public CustomResponse<?> deleteBook(@PathVariable Long id) throws Exception {
        try {
            bookService.deleteBookByID(id);
            return new CustomResponse<>(HttpStatus.ACCEPTED,"", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "", e.getMessage());
        }
    }

    @DeleteMapping("")
    public CustomResponse<?> deleteBook(@RequestBody BookDTO bookDTO) throws Exception {
        try {
            bookService.deleteBook(bookDTO);
            return new CustomResponse<>(HttpStatus.ACCEPTED,"", bookService.getAllBooks());
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "", e.getMessage());
        }
    }
}
