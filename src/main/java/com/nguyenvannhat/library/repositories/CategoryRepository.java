package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT b.* FROM books b" +
            "INNER JOIN book_categories bc ON bs.id = bc.id" +
            "INNER JOIN categories c ON c.id = bc.id" +
            "WHERE c.name = :#{#category.name}",nativeQuery = true)
    List<Book> findBooksByBookCategories(@Param("category") Category category);
    Optional<Category> findByName(String name);
}
