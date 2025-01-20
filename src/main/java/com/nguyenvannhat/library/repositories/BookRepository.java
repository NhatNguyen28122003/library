package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(
            "SELECT b FROM Book b WHERE b.title = :#{#title}"
    )
    List<Book> findBookByTitle(@Param("title") String title);
}
