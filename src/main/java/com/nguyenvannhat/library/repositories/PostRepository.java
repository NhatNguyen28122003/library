package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(
            "SELECT p FROM Post p " +
            "INNER JOIN UserPost up ON up.postId = p.id " +
            "INNER JOIN User u ON u.id = up.userId " +
            "WHERE u.username = :#{#user.username}"
    )
    Post findPostByUser(@Param("user") User user);

    Post findPostByTitle(String title);

    @Query("SELECT p FROM Post p WHERE p.totalLikes = (SELECT MAX(p.totalLikes) FROM Post p)")
    List<Post> getTopLikedPosts();
}
