package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(
            value = "SELECT c FROM categories c" +
                    "INNER JOIN book_categories bc ON c.id = bc.id" +
                    "INNER JOIN books c ON b.id = bc.id" +
                    "WHERE b.name = :#{#book.name}",nativeQuery = true
    )
    List<Category> findCategoriesByBook(@Param("book") Book book);
    Book findByTitle(String title);
    List<Book> findByAuthor(String author);
    void deleteByTitle(String title);
}
