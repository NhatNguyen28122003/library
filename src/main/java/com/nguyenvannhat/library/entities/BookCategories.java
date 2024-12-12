package com.nguyenvannhat.library.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_categories")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "book_id")
    private Long bookId;
}
