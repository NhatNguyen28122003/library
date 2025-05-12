package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCode(String code);

    @Query(
            "select distinct c from Category c " +
                    "where c.code in :codes"
    )
    List<Category> findByCodeIn(@Param("codes") List<String> codes);
}
