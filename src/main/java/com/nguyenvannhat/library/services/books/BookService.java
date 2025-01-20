package com.nguyenvannhat.library.services.books;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.responses.CustomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BookService {
    CustomResponse<List<Book>> addBook(BookDTO bookDTO);

    CustomResponse<List<Book>> addMultiBook(MultipartFile multipartFile) throws IOException;

    CustomResponse<List<Book>> findAllBook();

    CustomResponse<List<Book>> findBookByTitle(String title);

    CustomResponse<File> exportBooksToExcel() throws IOException;

    CustomResponse<List<Book>> deleteBookByTitle(String title);
}
