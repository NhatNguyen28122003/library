package com.nguyenvannhat.library.controllers.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.books.BookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final HttpServletRequest httpServletRequest;

    @GetMapping
    @PreAuthorize("fileRole()")  // Custom security expression
    public List<BookDTO> getAllBooks() throws Exception{
        return bookService.getAllBooks().stream().map(
                book -> new BookDTO(book.getTitle(), book.getAuthor(), book.getPages())
        ).collect(Collectors.toList());
    }

    @PostMapping("/insertBook")
    @PreAuthorize("fileRole()")
    public CustomResponse<?> insertBook(@RequestBody BookDTO bookDTO) throws Exception{
        bookService.insertBook(bookDTO);
        return new CustomResponse<>(HttpStatus.ACCEPTED, "Book created successfully!", bookDTO);
    }

    @PostMapping("/insertMultiBooks")
    @PreAuthorize("fileRole()")
    public CustomResponse<?> insertMultiBooks(@RequestBody List<BookDTO> bookDTOs) throws Exception{
        for (BookDTO bookDTO : bookDTOs) {
            bookService.insertBook(bookDTO);
        }
        return new CustomResponse<>(HttpStatus.ACCEPTED, "Books created successfully!", bookDTOs);
    }

    @PostMapping("/insert/importExcel")
    @PreAuthorize("fileRole()")
    public CustomResponse<?> importExcel(@RequestParam("file") MultipartFile file) throws Exception{
        bookService.insertBooks(file);
        return new CustomResponse<>(HttpStatus.ACCEPTED, "Books imported successfully!", bookService.getAllBooks());
    }

    @GetMapping("/read/exportExcel")
    @PreAuthorize("fileRole()")
    public CustomResponse<?> exportExcel(@RequestBody List<BookDTO> bookDTOS)throws Exception {
        bookService.exportBooksToExcel(bookDTOS);
        return new CustomResponse<>(HttpStatus.ACCEPTED, "Books exported successfully!", bookService.getAllBooks());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole()")
    public CustomResponse<?> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) throws Exception{
        bookService.updateBook(id, bookDTO);
        return new CustomResponse<>(HttpStatus.ACCEPTED, "Book updated successfully!", bookService.getAllBooks());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    public CustomResponse<?> deleteBook(@PathVariable Long id) throws Exception{
        bookService.deleteBookByID(id);
        return new CustomResponse<>(HttpStatus.ACCEPTED, "Book deleted successfully!", bookService.getAllBooks());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("fileRole()")
    public CustomResponse<?> deleteBook(@RequestBody BookDTO bookDTO) throws Exception{
        bookService.deleteBook(bookDTO);
        return new CustomResponse<>(HttpStatus.ACCEPTED, "Book deleted successfully!", bookService.getAllBooks());
    }
}
