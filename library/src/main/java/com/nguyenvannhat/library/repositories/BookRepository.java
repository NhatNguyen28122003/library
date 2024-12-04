package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Integer> {
    Book findByName(String name);
}
