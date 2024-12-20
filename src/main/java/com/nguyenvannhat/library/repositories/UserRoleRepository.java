package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.UserCustom;
import com.nguyenvannhat.library.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query(
            value = "SELECT ur.* FROM user_roles ur" +
                    "WHERE ur.role_id = :#{#role.id}", nativeQuery = true
    )
    Optional<UserRole> findByRole(@Param("role") Role role);

    @Query(
            value = "SELECT ur.* FROM user_roles ur" +
                    "WHERE ur.user_id = :#{#user.id}", nativeQuery = true
    )
    Optional<UserRole> findByUser(@Param("user") UserCustom userCustom);
}
