package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookCategories;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategories,Long> {

    @Query(
            "SELECT bc FROM BookCategories bc " +
                    "INNER JOIN Category c ON bc.categoryId = c.id " +
                    "WHERE c.id = :#{#category.id}"
    )
    List<BookCategories> findAllByCategory(@Param("category") Category category);

    @Query(
            "SELECT bc FROM BookCategories bc " +
                    "INNER JOIN Book b ON b.id = bc.bookId " +
                    "WHERE b.id = :#{#book.id}"
    )
    List<BookCategories> findAllByBook(@Param("book") Book book);

    @Modifying
    @Query(
            "DELETE FROM BookCategories bc " +
                    "WHERE bc.bookId = :#{#bookId}"
    )
    void deleteByBookId(@Param("bookId") Long bookId);

    @Modifying
    @Query(
            "DELETE FROM BookCategories bc " +
                    "WHERE bc.categoryId = :#{#categoryId}"
    )
    void deleteByCategoryId(@Param("categoryId") Long categoryId);
}
