package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    @Query(
            "SELECT pc FROM PostComment pc " +
                    "INNER JOIN Post p ON pc.postId = p.id " +
                    "WHERE p.id = :#{#post.id}"
    )
    List<PostComment> findByPost(@Param("post") Post post);


    @Query(
            "SELECT pc FROM PostComment pc " +
                    "INNER JOIN Comment c ON pc.commentId = c.id " +
                    "WHERE c.id = :#{#comment.id}"
    )
    List<PostComment> findByComment(@Param("comment") Comment comment);
}
