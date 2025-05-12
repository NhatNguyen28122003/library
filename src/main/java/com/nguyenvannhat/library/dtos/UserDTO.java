package com.nguyenvannhat.library.dtos;

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
    private Long id;
    private String userName;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String identityNumber;
    private LocalDate birthDay;
    private Integer age;
    private String address;
}
