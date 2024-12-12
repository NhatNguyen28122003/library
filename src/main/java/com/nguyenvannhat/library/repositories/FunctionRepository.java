package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Function;
import com.nguyenvannhat.library.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FunctionRepository extends JpaRepository<Function, Long> {
    Optional<Function> findByFunctionName(String name);
    @Query(value = "SELECT f.function_name FROM functions f" +
                    "INNER JOIN role_functions rf ON rf.function_id = f.id" +
                    "INNER JOIN roles r ON r.id = rf.role_id" +
                    "WHERE r.name = :roleName",nativeQuery = true)
    List<String> getFunctionsByRoleName(@Param("roleName") String roleName);
}
