package com.nguyenvannhat.library.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    @NotBlank(message = "Title can not be empty")
    private String title;
    private String body;
}
