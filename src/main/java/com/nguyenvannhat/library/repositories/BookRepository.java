package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByCode(String code);
}
