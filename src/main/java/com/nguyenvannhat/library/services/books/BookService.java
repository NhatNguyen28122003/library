package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.dtos.requests.BookRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    BookDTO addBook(BookRequest book);
    List<BookDTO> getAllBooks();
    BookDTO update(Long id, BookRequest book);
    BookDTO getBook(Long id);
    void delete(Long id);
    List<BookDTO> importExcel(MultipartFile file);
    byte[] exportExcel();

}
