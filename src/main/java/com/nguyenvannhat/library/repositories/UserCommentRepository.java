package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.entities.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCommentRepository extends JpaRepository<UserComment, Long> {
    @Query(
            "SELECT uc FROM UserComment uc " +
                    "INNER JOIN User u ON uc.userId = u.id " +
                    "INNER JOIN Comment c ON c.id = uc.commentId " +
                    "WHERE u.id = :#{#user.id}"
    )
    List<UserComment> findAllByUser(@Param("user") User user);
}
