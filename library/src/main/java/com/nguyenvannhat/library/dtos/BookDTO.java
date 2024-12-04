package com.nguyenvannhat.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    @JsonProperty
    private String name;

    @JsonProperty
    private String categoryName;
}
