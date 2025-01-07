package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("SELECT u FROM User u " +
            "INNER JOIN UserRole ur ON ur.userId = u.id " +
            "INNER JOIN Role r ON r.id = ur.roleId " +
            "WHERE r.id = :#{#role.id}")
    List<User> findUserByRole(@Param("role") Role role);

    @Query("SELECT r FROM Role r " +
            "INNER JOIN UserRole ur ON ur.roleId = r.id " +
            "INNER JOIN User u ON ur.userId = u.id " +
            "WHERE u.id = :#{#user.id}")
    Optional<Role> findRoleByUser(@Param("user") User user);
}
