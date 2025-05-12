package com.nguyenvannhat.library.services.bookloan;

import com.nguyenvannhat.library.entities.BlackList;
import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookLoan;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.repositories.BlackListRepository;
import com.nguyenvannhat.library.repositories.BookLoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {
    private final BookLoanRepository bookLoanRepository;
    private final BlackListRepository blackListRepository;

    @Override
    public BookLoan borrow(Book book, User user) {
        List<BlackList> blackLists = blackListRepository.findAll();
        String userName = user.getUserName();
        List<String> userNameInBlackList = blackLists.stream().map(BlackList::getUserName).collect(Collectors.toList());
        if (userNameInBlackList.contains(userName)) {
            log.info("User {} is already in black list", userName);
            return null;
        }

        BookLoan bookLoan = BookLoan.builder()
                .bookId(book.getId())
                .userId(user.getId())
                .build();
        bookLoan = bookLoanRepository.save(bookLoan);
        return bookLoan;
    }

    @Override
    public BookLoan returnBook(Book book, User user) {
        List<Book> borrowed = bookLoanRepository.findBookByUserId(user.getId());
        BookLoan bookLoan = new BookLoan();
        if (borrowed.contains(book)) {
            bookLoan = bookLoanRepository.findByBookIdAndUserId(book.getId(), user.getId());
            bookLoanRepository.delete(bookLoan);
        }
        return bookLoan;
    }

    @Override
    public BlackList addUserToBlackList(User user) {
        List<BlackList> blackLists = blackListRepository.findAll();
        String userName = user.getUserName();
        if (blackLists.contains(user)) {
            return null;
        }
        BlackList newBlackList = BlackList.builder()
                .userName(userName)
                .build();
       newBlackList = blackListRepository.save(newBlackList);
        return newBlackList;
    }
}
