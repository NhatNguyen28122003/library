package com.nguyenvannhat.library.services.comments;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> getAllComments();

    Comment getCommentById(Long id);

    List<Comment> getCommentByPost(Post post);

    Optional<Comment> addComment(Comment comment);

    Optional<Comment> updateComment(Comment comment);

    void deleteComment(Comment comment);

    void deleteById(long id);
}
