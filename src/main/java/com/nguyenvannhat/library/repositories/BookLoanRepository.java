package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookLoan;
import com.nguyenvannhat.library.entities.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

    @Query("SELECT b FROM Book b " +
            "INNER JOIN BookLoan bl ON bl.bookId = b.id " +
            "INNER JOIN UserCustom u ON u.id = bl.userId " +
            "WHERE u.id = :#{#user.id}")
    List<Book> getBookByUser(@Param("user") UserCustom userCustom);

    @Query("SELECT bl FROM BookLoan bl " +
            "INNER JOIN Book b ON b.id = bl.bookId " +
            "INNER JOIN UserCustom u ON bl.userId = u.id " +
            "WHERE u.id = :#{#userId} AND b.id = :#{#bookId}")
    BookLoan getBookLoanByUserIdAndBookId(@Param("userId") Long userId,@Param("bookId") Long bookId);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.bookId = :#{#bookId}")
    List<BookLoan> getBookLoanByBookId(@Param("bookId") Long bookId);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.userId = :#{#userId}")
    List<BookLoan> getBookLoanByUserId(@Param("userId") Long userId);
}
