package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    List<User> findByFullName(String fullName);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByBirthDay(LocalDate birthDay);

    List<User> findByAddress(String address);

    List<User> findByAge(Integer age);
}
