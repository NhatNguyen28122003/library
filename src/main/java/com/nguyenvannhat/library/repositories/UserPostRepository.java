package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.entities.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {

    @Query(
            "SELECT up FROM UserPost up " +
                    "INNER JOIN User u ON up.userId = u.id " +
                    "INNER JOIN Post p ON p.id = up.postId " +
                    "WHERE p.id = :#{#post.id}"
    )
    List<UserPost> findAllByPost(@Param("post") Post post);

    @Query(
            "SELECT up FROM UserPost up " +
                    "INNER JOIN Post p ON p.id = up.postId " +
                    "INNER JOIN User u ON u.id = up.userId " +
                    "WHERE u.id = :#{#user.id}"
    )
    List<UserPost> findAllByUser(@Param("user") User user);
}
