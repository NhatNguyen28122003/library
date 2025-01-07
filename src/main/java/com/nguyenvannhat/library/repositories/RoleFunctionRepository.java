package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Function;
import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.RoleFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleFunctionRepository extends JpaRepository<RoleFunction, Long> {
    @Query("SELECT f FROM Function f " +
            "INNER JOIN RoleFunction rf ON rf.functionId = f.id " +
            "INNER JOIN Role r ON r.id = rf.roleId " +
            "WHERE r.id = :#{#role.id}")
    List<Function> findByRole(@Param("role") Role role);

}
