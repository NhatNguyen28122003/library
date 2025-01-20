package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query(
            "SELECT ur FROM UserRole ur WHERE ur.userId = :#{#userId}"
    )
    List<UserRole> findByUserId(@Param("userId") Long userId);

    @Query(
            "SELECT ur FROM UserRole ur WHERE ur.userId = :#{#roleId}"
    )
    List<UserRole> findByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT r FROM Role r " +
            "INNER JOIN UserRole ur ON ur.roleId = r.id " +
            "INNER JOIN User u ON ur.userId = u.id " +
            "WHERE u.id = :#{#user.id}")
    List<Role> findRolesByUser(@Param("user") User user);

    @Query("SELECT u FROM User u " +
            "INNER JOIN UserRole ur ON ur.userId = u.id " +
            "INNER JOIN Role r ON ur.roleId = r.id " +
            "WHERE r.id = :#{#role.id}")
    List<User> findUsersByRole(@Param("role") Role role);
}
