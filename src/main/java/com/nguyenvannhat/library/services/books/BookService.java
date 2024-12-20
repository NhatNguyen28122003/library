package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    void insertBook(BookDTO bookDTO) throws  Exception;
    void insertBooks(MultipartFile multipartFile) throws Exception;
    BookDTO getBookById(Long id)throws Exception;
    List<BookDTO> findAllBooksByCategory(Category category);
    void updateBook(Long id, BookDTO bookDTO) throws Exception;
    void deleteBook(BookDTO bookDTO) throws Exception;
    File exportBooksToExcel(List<BookDTO> bookDTO) throws Exception;
    void deleteBookByID(Long id) ;
}
