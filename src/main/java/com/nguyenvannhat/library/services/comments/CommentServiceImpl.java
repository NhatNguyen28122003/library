package com.nguyenvannhat.library.services.comments;

import com.nguyenvannhat.library.dtos.CommentDTO;
import com.nguyenvannhat.library.repositories.CommentRepository;
import com.nguyenvannhat.library.repositories.PostCommentRepository;
import com.nguyenvannhat.library.repositories.UserCommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserCommentRepository userCommentRepository;
    private final ModelMapper modelMapper;

    @Override
    public CommentDTO create(CommentDTO comment) {
        return null;
    }

    @Override
    public List<CommentDTO> getAllComments() {
        return List.of();
    }

    @Override
    public List<CommentDTO> getAllCommentsByUserId(Long userId) {
        return List.of();
    }

    @Override
    public void deleteComment(Long id) {

    }
}
