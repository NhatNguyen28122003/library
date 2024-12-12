package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

}
