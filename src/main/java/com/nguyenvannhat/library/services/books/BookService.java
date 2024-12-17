package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.exceptions.DataNotFoundException;
import com.nguyenvannhat.library.exceptions.InvalidDataException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    void insertBook(BookDTO bookDTO) throws InvalidDataException;
    void insertBooks(MultipartFile multipartFile) throws InvalidDataException;
    void updateBook(Long id, BookDTO bookDTO) throws DataNotFoundException;
    void deleteBook(BookDTO bookDTO) throws DataNotFoundException;
    void exportBooksToExcel(List<BookDTO> bookDTO) throws DataNotFoundException;
    void deleteBookByID(Long id) ;
}
