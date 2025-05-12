package com.nguyenvannhat.library.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookRequest {
    @NotBlank(message = "Book code must not be blank")
    private String code;

    private String title;

    private String author;

    private String url;

    private int pages;

    List<String> categoryCode;
}
