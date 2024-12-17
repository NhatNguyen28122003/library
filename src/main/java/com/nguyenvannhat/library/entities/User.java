package com.nguyenvannhat.library.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_name", unique = true, nullable = false)
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "identity_number",unique = true, nullable = false)
    private Long identityNumber;
    @Column(name = "bitrh_day")
    private LocalDate birthDay;
    @Column(name = "age")
    private Integer age;
    @Column(name = "address")
    private String address;

    @Column(name = "is_borrowed")
    private boolean isBorrowed = true;
}
