package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByTitle(String title);

    @Query("SELECT p FROM Post p " +
            "INNER JOIN UserPost up ON up.postId = p.id " +
            "INNER JOIN User u ON u.id = up.userId " +
            "WHERE u.fullName = #{#user.fullName}")
    Post findPostByUser(@Param("user") User user);
}
