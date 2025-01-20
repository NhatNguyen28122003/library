package com.nguyenvannhat.library.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotNull(message = "Username must not be empty!")
    private String userName;

    @NotNull(message = "Password length must be >= 8!")
    @Min(value = 8)
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String identityNumber;
    private LocalDate birthDay;
    private Integer age;
    private String address;
}
