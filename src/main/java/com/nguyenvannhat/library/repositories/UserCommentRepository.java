package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.UserComment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCommentRepository extends JpaRepository<UserComment, Long> {
    void deleteByUserId(Long userId);
    void deleteByCommentId(Long commentId);
    void deleteByUserIdAndCommentId(Long userId, Long commentId);
    @Modifying
    @Transactional
    @Query(
            "DELETE FROM UserComment uc " +
                    "WHERE uc.commentId IN ( " +
                    "SELECT c.id FROM Comment c " +
                    "INNER JOIN PostComment pc ON pc.commentId = c.id " +
                    "INNER JOIN Post p ON p.id = pc.postId " +
                    "WHERE p.id = :#{#postId})"
    )
    void deleteByPostId(@Param("postId") Long postId);
}
