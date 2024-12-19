package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
}
