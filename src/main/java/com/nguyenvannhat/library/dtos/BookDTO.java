package com.nguyenvannhat.library.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {


    @NotNull(message = "Title not be empty!")
    private String title;
    private String author;
    private int pages;
    private List<String> categoryName;

    public BookDTO(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }
}
