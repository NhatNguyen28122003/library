package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByIdentityNumber(String identityNumber);

    List<User> findByFullName(String fullName);
    List<User> findByBirthDay(LocalDate birthday);
    List<User> findByAge(Integer age);
    List<User> findByAddress(String address);

    void deleteByUserName(String username);
    void deleteByEmail(String email);
    void deleteByPhoneNumber(String phoneNumber);
    void deleteByIdentityNumber(String identityNumber);
}
