package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    void deleteByPostId(Long postId);
    void deleteByUserId(Long userId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
