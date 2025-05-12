package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(
            "SELECT c FROM Comment c " +
                    "INNER JOIN UserComment uc ON uc.commentId = c.id " +
                    "INNER JOIN User u ON u.id = uc.userId " +
                    "WHERE u.id = :#{#author.id} and u.isDeleted = false "
    )
    List<Comment> findAllByAuthor(@Param("author") User author);

    @Query(
            "select c from Comment c " +
                    "join PostComment  pc on pc.commentId = c.id " +
                    "join Post p on p.id = pc.postId " +
                    "where p.id = :postId and p.isDeleted = false  and c.isDeleted = false "
    )
    List<Comment> findByPostId(@Param("postId") Long postId);

    List<Comment> findByIdIn(Collection<Long> ids);
}
