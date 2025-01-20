package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByTitle(String title);

    @Query(
            "SELECT p FROM Post p " +
                    "INNER JOIN UserPost up ON up.postId = p.id " +
                    "INNER JOIN User u ON up.userId = u.id " +
                    "WHERE u.id = :#{#user.id}"
    )
    List<Post> findPostsByUser(@Param("user") User user);

    @Query(
            "SELECT c FROM Comment c " +
                    "INNER JOIN PostComment pc ON pc.commentId = c.id " +
                    "INNER JOIN Post p ON pc.postId = p.id " +
                    "WHERE p.id = :#{#post.id}"
    )
    List<Comment> findCommentsByPost(@Param("post") Post post);
}
