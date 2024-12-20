package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserCustom, Long> {
    Optional<UserCustom> findByUsername(String username);

    List<UserCustom> findByFullName(String fullName);

    List<UserCustom> findByEmail(String email);

    Optional<UserCustom> findByPhoneNumber(String phoneNumber);

    List<UserCustom> findByBirthDay(LocalDate birthDay);

    @Query("SELECT f.functionName FROM Function f " +
            "JOIN RoleFunction rf ON rf.functionId = f.id " +
            "JOIN Role r ON r.id = rf.roleId " +
            "WHERE r.id = :roleId")
    List<Object> getFunctionRoleName(@Param("roleId") Long roleId);

    @Query("SELECT r FROM Role r " +
            "JOIN UserRole ur ON r.id = ur.roleId " +
            "JOIN UserCustom u ON ur.userId = u.id " +
            "WHERE u.username = :userName")
    Role getRoleByUserName(@Param("userName") String userName);
}
