package com.nguyenvannhat.library.repositories;

import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    void deleteByPostId(Long postId);

    void deleteByCommentId(Long commentId);

    void deleteByPostIdAndCommentId(Long postId, Long commentId);
}
