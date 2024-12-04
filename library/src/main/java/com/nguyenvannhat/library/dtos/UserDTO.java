package com.nguyenvannhat.library.dtos;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String phoneNumber;
    private String password;
    private String role;

    public UserDTO(String fullName, String phoneNumber) {
        name = fullName;
        this.phoneNumber = phoneNumber;
    }
}
