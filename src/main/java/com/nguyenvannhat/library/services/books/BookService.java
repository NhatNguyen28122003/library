package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Book insertBook(BookDTO bookDTO) throws Exception;
    List<Book> insertBooks(MultipartFile multipartFile) throws Exception;
    Book updateBook(Long id,BookDTO bookDTO) throws Exception;
    void deleteBook(BookDTO bookDTO) throws Exception;
    void exportBooksToExcel(List<BookDTO> bookDTO) throws Exception;
    void deleteBookByID(Long id) throws Exception;
}
