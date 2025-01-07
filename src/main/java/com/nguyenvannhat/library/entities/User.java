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

    @Column
    private String fullName;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private Long identityNumber;

    @Temporal(TemporalType.DATE)
    private LocalDate birthDay;

    private Integer age;

    private String address;

    private Boolean isActive;

    private Boolean isDelete;

    private Boolean isBorrowed;


    @Override
    protected void onCreate() {
        super.onCreate();
        isActive = true;
        isDelete = false;
        isBorrowed = true;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
