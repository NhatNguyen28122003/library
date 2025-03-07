package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    @Query(
            "SELECT c FROM Category c WHERE c.name = :#{#name}"
    )
    Optional<Category> findByName(@Param("name") String name);
}
