package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    void insertBook(BookDTO bookDTO) throws InvalidDataException, Exception;
    void insertBooks(MultipartFile multipartFile) throws InvalidDataException;
    BookDTO getBookById(Long id)throws DataNotFoundException;
    List<BookDTO> findAllBooksByCategory(Category category);
    void updateBook(Long id, BookDTO bookDTO) throws DataNotFoundException;
    void deleteBook(BookDTO bookDTO) throws DataNotFoundException;
    File exportBooksToExcel(List<BookDTO> bookDTO) throws DataNotFoundException, FileNotFoundException;
    void deleteBookByID(Long id) ;
}
