package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.UserCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(
            "SELECT cm FROM Comment cm " +
                    "INNER JOIN UserComment usc ON usc.commentId = cm.id " +
                    "INNER JOIN UserCustom u ON u.id = usc.userId " +
                    "WHERE u.username = :#{#user.username}"
    )
    List<Comment> findByUser(@Param("user") UserCustom userCustom);

    @Query(
            "SELECT u FROM UserCustom u " +
                    "INNER JOIN UserComment usc ON u.id = usc.userId " +
                    "INNER JOIN Comment c ON c.id = usc.commentId " +
                    "WHERE c.id = :#{#comment.id}"
    )
    UserCustom findByComment(@Param("comment") Comment comment);

    @Query("SELECT c FROM Comment c " +
            "INNER JOIN PostComment pc ON pc.commentId = c.id " +
            "INNER JOIN Post p ON p.id = pc.postId " +
            "WHERE p.id = :postId")
    List<Comment> findCommentByPostId(@Param("postId") Long postId);

    @Query(
            "SELECT c FROM Comment c " +
                    "INNER JOIN PostComment pc ON pc.commentId = c.id " +
                    "INNER JOIN Post p ON p.id = pc.postId " +
                    "WHERE p.id = :#{#post.id}"
    )
    List<Comment> findCommentByPost(@Param("post") Post post);
}
