package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.dtos.CategoryDTO;
import com.nguyenvannhat.library.models.Book;
import com.nguyenvannhat.library.models.Category;
import com.nguyenvannhat.library.repositories.BookRepository;

import java.util.List;

public interface BookService {
    Book insertBook(BookDTO bookDTO);

    Book updateBook(int id, BookDTO bookDTO);

    List<Book> getAllBooks();

    Book getById(int id);

    Book getByName(String name);

    void deleteById(int id);

    void deleteByName(String name);
}
