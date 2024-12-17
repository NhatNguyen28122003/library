package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(
            "SELECT c FROM Category c " +
                    "INNER JOIN BookCategories bc ON bc.categoryId = c.id " +
                    "INNER JOIN Book b ON b.id = bc.bookId " +
                    "WHERE b.id = :#{#book.id}"
    )
    List<Category> findCategoriesByBook(@Param("book") Book book);
    Optional<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    void deleteByTitle(String title);
}
