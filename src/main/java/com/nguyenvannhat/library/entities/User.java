package com.nguyenvannhat.library.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userName;

    private String password;

    private String fullName;

    private String phoneNumber;

    private String email;

    private String identityNumber;

    @Temporal(TemporalType.DATE)
    private LocalDate birthDay;

    private Integer age;

    private String address;

    private Boolean isBorrowed;

}
