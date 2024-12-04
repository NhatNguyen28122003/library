package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByFullName(String name);

    Page<User> findAll(Pageable pageable);
}
