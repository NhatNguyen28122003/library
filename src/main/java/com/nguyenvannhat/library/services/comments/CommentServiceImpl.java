package com.nguyenvannhat.library.services.comments;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.exceptions.DataNotFoundException;
import com.nguyenvannhat.library.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comment getCommentById(Long id) throws Exception{
        return commentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Comment not found"));
    }

    @Override
    public List<Comment> getCommentByPost(Post post) {
        return commentRepository.findCommentByPost(post);
    }

    @Override
    public Optional<Comment> addComment(Comment comment) {
        Comment newComment = commentRepository.save(comment);
        return Optional.of(newComment);
    }

    @Override
    public Optional<Comment> updateComment(Comment comment) {
        Comment existingComment = commentRepository.findById(comment.getId()).orElse(null);
        if (existingComment != null) {
            existingComment.setComment(comment.getComment());
            Comment updatedComment = commentRepository.save(existingComment);
            return Optional.of(updatedComment);
        }
        return Optional.empty();
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
