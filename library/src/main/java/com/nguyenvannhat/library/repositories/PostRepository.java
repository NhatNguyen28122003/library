package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {
    Optional<Post> findByTitle(String title);
}
