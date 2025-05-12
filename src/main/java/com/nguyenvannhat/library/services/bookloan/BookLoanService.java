package com.nguyenvannhat.library.services.bookloan;

import com.nguyenvannhat.library.entities.BlackList;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookLoan;
import com.nguyenvannhat.library.entities.User;

public interface BookLoanService {
    BookLoan borrow(Book book, User user);

    BookLoan returnBook(Book book, User user);

    BlackList addUserToBlackList(User user);
}
