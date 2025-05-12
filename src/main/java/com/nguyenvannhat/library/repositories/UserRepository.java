package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    @Query(
            "select distinct u from User u " +
                    "where u.isDeleted = false " +
                    "and ( cast(u.id as string ) like %:keyword% " +
                    "or u.userName like %:keyword% " +
                    "or u.phoneNumber like %:keyword% " +
                    "or u.email like %:keyword% " +
                    "or u.fullName like %:keyword% " +
                    ")"
    )
    Optional<User> search(@Param("keyword") String keyword);
}
