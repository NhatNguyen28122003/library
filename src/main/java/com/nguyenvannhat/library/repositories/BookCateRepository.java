package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Book;
import com.nguyenvannhat.library.entities.BookCategories;
import com.nguyenvannhat.library.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookCateRepository extends JpaRepository<BookCategories, Long> {

    @Query(
            "SELECT bc FROM BookCategories bc WHERE bc.categoryId = :#{#category.id}"
    )
    List<BookCategories> findAllByCategory(Category category);

    @Query(
            "SELECT bc FROM BookCategories bc WHERE bc.bookId = :#{#book.id}"
    )
    List<BookCategories> findAllByBook(Book book);

    @Query(
            "SELECT b FROM Book b " +
                    "INNER JOIN BookCategories bc ON bc.bookId = b.id " +
                    "INNER JOIN Category c ON c.id = bc.categoryId " +
                    "WHERE c.id = :#{#category.id}"
    )
    List<Book> findBooksByCategory(Category category);

    @Query(
            "SELECT c FROM Category c " +
                    "INNER JOIN BookCategories bc ON bc.categoryId = c.id " +
                    "INNER JOIN Book b ON b.id = bc.bookId " +
                    "WHERE b.id = :#{#book.id}"
    )
    List<Category> findCategoriesByBook(Book book);
}
