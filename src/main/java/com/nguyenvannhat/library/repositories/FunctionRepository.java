package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FunctionRepository extends JpaRepository<Function, Long> {

    List<Function> findByIsDeletedIsFalse();

    Optional<Function> findByCode(String lowerCase);
    @Query(
            "select distinct f from Function f " +
                    "where f.code = :keyword " +
                    "or f.name = :keyword " +
                    "or f.description = :keyword " +
                    "and f.isDeleted = false "
    )
    List<Function> search(String keyword);
    @Query(
            "select distinct f from Function f " +
                    "join RoleFunction rf on rf.functionId = f.id " +
                    "join Role r on r.id = rf.roleId " +
                    "where r.id in :roleIds"
    )
    List<Function> findByRoleIdIn(@Param("roleIds") List<Long> roleIds);
}


