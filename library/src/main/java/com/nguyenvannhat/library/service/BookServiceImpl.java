package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.models.Book;
import com.nguyenvannhat.library.models.Category;
import com.nguyenvannhat.library.repositories.BookRepository;
import com.nguyenvannhat.library.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Book insertBook(BookDTO bookDTO) {
        if (bookRepository.findByName(bookDTO.getName()) != null) {
            throw new RuntimeException("Book's exist");
        }
        Book book = Book.builder()
                .id(0)
                .name(bookDTO.getName())
                .category(categoryRepository.findByName(bookDTO.getCategoryName()).get())
                .build();
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(int id, BookDTO bookDTO) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Book does not exist!");
        }
        try {
            Book book = bookRepository.findById(id).get();
            book.setName(bookDTO.getName());
            book.setCategory(categoryRepository.findByName(bookDTO.getCategoryName()).get());
            return bookRepository.save(book);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(int id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Not find book by id: " + id));
    }

    @Override
    public Book getByName(String name) {
        return bookRepository.findByName(name);
    }

    @Override
    public void deleteById(int id) {
        if (bookRepository.findById(id).isEmpty()) {
            return;
        }
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        if (bookRepository.findByName(name) == null) {
            return;
        }
        int id = bookRepository.findByName(name).getId();
        bookRepository.deleteById(id);
    }
}
