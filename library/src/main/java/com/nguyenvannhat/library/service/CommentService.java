package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> findAllComments();
    
}
