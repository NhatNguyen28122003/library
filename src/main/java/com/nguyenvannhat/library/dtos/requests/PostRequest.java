package com.nguyenvannhat.library.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PostRequest {
    @NotBlank(message = "Post code must not be blank!")
    String code;
    private String title;
    private String body;

}
