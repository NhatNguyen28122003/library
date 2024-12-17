package com.nguyenvannhat.library.controllers.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.bookloan.BookLoanService;
import com.nguyenvannhat.library.services.books.BookService;
import com.nguyenvannhat.library.services.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookLoanService bookLoanService;
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    @GetMapping
    @PreAuthorize("fileRole()")  // Custom security expression
    public ResponseEntity<?> getAllBooks() {
        return CustomResponse.success(bookService.getAllBooks());
    }

    @PostMapping("/insertBook")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> insertBook(@RequestBody BookDTO bookDTO) throws Exception{
        bookService.insertBook(bookDTO);
        return CustomResponse.success(bookService.getAllBooks());
    }

    @PostMapping("/insertMultiBooks")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> insertMultiBooks(@RequestBody List<BookDTO> bookDTOs) throws Exception{
        for (BookDTO bookDTO : bookDTOs) {
            bookService.insertBook(bookDTO);
        }
        return CustomResponse.success(bookService.getAllBooks());
    }

    @PostMapping("/insert/importExcel")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) throws Exception{
        bookService.insertBooks(file);
        return CustomResponse.success(bookService.getAllBooks());
    }

    @GetMapping("/read/exportExcel")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> exportExcel(@RequestBody List<BookDTO> bookDTOS)throws Exception {
        File file = bookService.exportBooksToExcel(bookDTOS);
        return CustomResponse.download(file);
    }

    @GetMapping("/read/booksByUser")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> getListBooksByUser() throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
       List<BookDTO> bookDTOS = bookLoanService.getListBooksByUser(user);
       return CustomResponse.success(bookDTOS);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) throws Exception{
        bookService.updateBook(id, bookDTO);
        return CustomResponse.success(bookService.getAllBooks());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBookByID(id);
        return CustomResponse.success(bookService.getAllBooks());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> deleteBook(@RequestBody BookDTO bookDTO) throws Exception{
        bookService.deleteBook(bookDTO);
        return CustomResponse.success(bookService.getAllBooks());
    }
}
