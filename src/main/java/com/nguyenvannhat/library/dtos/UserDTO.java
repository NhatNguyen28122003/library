package com.nguyenvannhat.library.dtos;

import jakarta.validation.constraints.NotBlank;
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
    private String username;
    @NotNull(message = "Password must not be empty!")
    private String password;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Long identityNumber;
    private LocalDate birthDay;
    private Integer age;
    private String address;
}
