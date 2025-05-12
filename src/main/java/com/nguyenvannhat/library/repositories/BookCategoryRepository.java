package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategories, Long> {
    @Query(
            "select b from Book b " +
                    "join BookCategories bc on bc.bookId = b.id " +
                    "join Category c on c.id = bc.categoryId " +
                    "where c.id = :id and c.isDeleted = false"
    )
    List<Book> findByCategoryId(@Param("id") Long id);
}
