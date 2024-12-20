package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    void insertBook(BookDTO bookDTO);
    void insertBooks(MultipartFile multipartFile) throws IOException;
    BookDTO getBookById(Long id);
    List<BookDTO> findAllBooksByCategory(Category category);
    void updateBook(Long id, BookDTO bookDTO);
    void deleteBook(BookDTO bookDTO);
    File exportBooksToExcel(List<BookDTO> bookDTO) throws IOException;
    void deleteBookByID(Long id) ;
}
