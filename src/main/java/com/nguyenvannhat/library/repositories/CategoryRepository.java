package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);

    @Query(
            "SELECT b FROM Book b " +
                    "INNER JOIN BookCategories bc ON bc.bookId = b.id " +
                    "INNER JOIN Category c ON bc.categoryId = c.id " +
                    "WHERE c.id = :#{#category.id}"
    )
    List<Book> findBooksByCategory(@Param("category") Category category);
    Optional<Category> findByName(String name);
    void deleteByName(String name);
    void deleteById(Long id);
}
