package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks();
    BookDTO getBook(Long id);
    List<Category> getCategoriesByBook(Book book);
    Book addBook(BookDTO bookDTO);
    List<BookDTO> addMultipleBooks(MultipartFile files) throws IOException;
    File exportBooksToWorkbook() throws IOException;
    Book updateBook(Long id, BookDTO bookDTO);
    void deleteBook(Long id);

}
