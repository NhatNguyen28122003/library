package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByFullName(String fullName);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByBirthDay(LocalDate birthDay);
    @Query("SELECT f.functionName FROM Function f " +
            "JOIN RoleFunction rf ON rf.functionId = f.id " +
            "JOIN Role r ON r.id = rf.roleId " +
            "WHERE r.id = :roleId")
    List<String> getFunctionRoleName(@Param("roleId") Long roleId);

    @Query("SELECT r FROM Role r " +
            "JOIN UserRole ur ON r.id = ur.roleId " +
            "JOIN User u ON ur.userId = u.id " +
            "WHERE u.username = :userName")
    Role getRoleByUserName(@Param("userName") String userName);

}
