package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

    BookLoan findByBookId(Long bookId);

    BookLoan findByUserId(Long userId);

    @Query(
            "select b from Book b " +
                    "join BookLoan bl on bl.bookId = b.id " +
                    "join User u on u.id = bl.userId " +
                    "where u.id = :userId and u.isDeleted = false "
    )
    List<Book> findBookByUserId(@Param("userId") Long userId);

    BookLoan findByBookIdAndUserId(Long bookId, Long userId);
}
