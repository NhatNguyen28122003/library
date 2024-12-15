package com.nguyenvannhat.library.repositories;
import com.nguyenvannhat.library.entities.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

}
