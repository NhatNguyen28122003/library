package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Optional<Category> findByName(String name);
    @Query("SELECT b.category.name, COUNT(b) FROM Book b GROUP BY b.category")
    List<Object[]> countBooksByCategory();
}
