package com.nguyenvannhat.library.services.comments;

import com.nguyenvannhat.library.dtos.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO create(CommentDTO comment);
    List<CommentDTO> getAllComments();
    List<CommentDTO> getAllCommentsByUserId(Long userId);
    void deleteComment(Long id);
}
