package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(
            "SELECT c FROM Comment c " +
                    "INNER JOIN UserComment uc ON uc.commentId = c.id " +
                    "INNER JOIN User u ON u.id = uc.userId " +
                    "WHERE u.id = :#{#author.id}"
    )
    List<Comment> findAllByAuthor(@Param("author") User author);
}
