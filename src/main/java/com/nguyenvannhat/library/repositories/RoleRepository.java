package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByCode(String code);

    @Query(
            "select r from Role r where r.isDeleted = false"
    )
    List<Role> findByIsDeleted();

    @Query(
            "select distinct r from Role r " +
                    "where r.code = :keyword " +
                    "or r.name = :keyword " +
                    "or r.description = :keyword " +
                    "and r.isDeleted = false "
    )
    List<Role> search(@Param("keyword") String keyword);

    @Query(
            "select distinct r from Role r " +
                    "join UserRole ur on ur.roleId = r.id " +
                    "join User u on u.id = ur.userId " +
                    "where u.id = :userId"
    )
    List<Role> findByUserIdAndIsDeletedIsFalse(@Param("userId") Long userId);
}
