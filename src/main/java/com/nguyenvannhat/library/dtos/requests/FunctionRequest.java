package com.nguyenvannhat.library.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FunctionRequest {
    @NotBlank(message = "Function Code must not be blank!")
    String code;
    String name;
    String description;
}
