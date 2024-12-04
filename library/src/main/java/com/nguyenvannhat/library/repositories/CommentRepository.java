package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.models.Comment;
import com.nguyenvannhat.library.models.Post;
import com.nguyenvannhat.library.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
