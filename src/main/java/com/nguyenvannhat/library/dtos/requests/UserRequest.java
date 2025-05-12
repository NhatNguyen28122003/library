package com.nguyenvannhat.library.dtos.requests;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "User Name must not be blank!")
    private String userName;

    private String password;

    private String fullName;

    @Size(min = 10, max = 10, message = "Phone number length must be 10!")
    private String phoneNumber;

    private String email;

    private String identityNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate birthDay;

    private Integer age;

    private String address;
}
