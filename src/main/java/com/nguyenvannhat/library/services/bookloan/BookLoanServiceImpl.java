package com.nguyenvannhat.library.services.bookloan;

import com.nguyenvannhat.library.dtos.BookDTO;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookLoan;
import com.nguyenvannhat.library.entities.UserCustom;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.exceptions.ErrorCode;
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
            UserCustom userCustom = userRepository.findByUsername(auth.getName()).orElseThrow(
                    () -> new ApplicationException(ErrorCode.USER_NOT_FOUND)
            );
            boolean isBorrowed = userCustom.getIsBorrowed().booleanValue();
            if (isBorrowed) {
                BookLoan bookLoan = BookLoan.builder()
                        .userId(userCustom.getId())
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
        UserCustom userCustom = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new ApplicationException(ErrorCode.USER_NOT_FOUND)
        );
        List<Book> books = bookLoanRepository.getBookByUser(userCustom);
        if (books.contains(book)) {
            book.setQuantity(book.getQuantity() + 1);
            BookLoan bookLoan = bookLoanRepository.getBookLoanByUserIdAndBookId(userCustom.getId(), book.getId());
            bookRepository.deleteById(bookLoan.getId());
        }
    }

    @Override
    public List<String> getBlackList() {
        List<UserCustom> userCustoms = userRepository.findAll();
        List<String> blackList = new ArrayList<>();
        for (UserCustom u : userCustoms) {
            boolean isBorrowed = u.getIsBorrowed().booleanValue();
            if (!isBorrowed) {
                blackList.add(u.getFullName());
            }
        }
        return blackList;
    }

    @Override
    public void addUserBlackList(UserCustom userCustom) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userCustom.setUpdateBy(auth.getName());
        userCustom.setIsBorrowed(false);
        userRepository.save(userCustom);
    }

    @Override
    public void removeUserBlackList(UserCustom userCustom) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userCustom.setUpdateBy(auth.getName());
        userCustom.setIsBorrowed(true);
        userRepository.save(userCustom);
    }

    @Override
    public List<BookDTO> getListBooksByUser(UserCustom userCustom) {
        return bookLoanRepository.getBookByUser(userCustom).stream().map(
                book -> new BookDTO(book.getTitle(), book.getAuthor(),book.getPages())
        ).toList();
    }
}
