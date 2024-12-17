package com.nguyenvannhat.library.services.bookloan;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookLoan;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.repositories.BookLoanRepository;
import com.nguyenvannhat.library.repositories.BookRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookLoanRepository bookLoanRepository;

    @Override
    public void borrowBook(Book book) {
        if (book.getQuantity() > 0) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(auth.getName()).orElseThrow(
                    () -> new RuntimeException("User not found")
            );
            if (user.getIsBorrowed()) {
                BookLoan bookLoan = BookLoan.builder()
                        .userId(user.getId())
                        .bookId(book.getId())
                        .borrowDate(new Date(System.currentTimeMillis()))
                        .build();
                bookLoanRepository.save(bookLoan);
                book.setQuantity(book.getQuantity() - 1);
                bookRepository.save(book);
            }
        }
    }

    @Override
    public void returnBook(Book book) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        List<Book> books = bookLoanRepository.getBookByUser(user);
        if (books.contains(book)) {
            book.setQuantity(book.getQuantity() + 1);
            BookLoan bookLoan = bookLoanRepository.getBookLoanByUserIdAndBookId(user.getId(), book.getId());
            bookRepository.deleteById(bookLoan.getId());
        }
    }

    @Override
    public List<User> getBlackList(User user) {
        List<User> users = userRepository.findAll();
        List<User> blackList = new ArrayList<>();
        for (User u : users) {
            if (!u.getIsBorrowed()) {
                blackList.add(u);
            }
        }
        return blackList;
    }

    @Override
    public void addUserBlackList(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        user.setUpdateBy(auth.getName());
        user.setIsBorrowed(false);
        userRepository.save(user);
    }
}
