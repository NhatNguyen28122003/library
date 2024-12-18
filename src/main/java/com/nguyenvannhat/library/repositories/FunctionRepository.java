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
    @Query(
            "SELECT f FROM Function f " +
                    "INNER JOIN RoleFunction rf ON rf.functionId = f.id " +
                    "INNER JOIN Role r ON r.id = rf.id " +
                    "WHERE r.id = :#{#role.id}"
    )
    List<String> getFunctionsByRole(@Param("role") Role role);
}
