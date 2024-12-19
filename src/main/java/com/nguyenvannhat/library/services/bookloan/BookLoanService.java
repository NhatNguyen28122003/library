package com.nguyenvannhat.library.services.bookloan;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.User;

import java.util.List;

public interface BookLoanService {
    void borrowBook(Book book);
    void returnBook(Book book);
    List<String> getBlackList();
    void addUserBlackList(User user);
    void removeUserBlackList(User user);
    List<BookDTO> getListBooksByUser(User user);
}
