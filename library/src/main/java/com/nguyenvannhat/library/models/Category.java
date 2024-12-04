package com.nguyenvannhat.library.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @Column(name = "name")
        private String name;

        @OneToMany(mappedBy = "category")
        private Set<Book> books;
}
