package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.UserPostDTO;
import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.models.Post;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();

    Post findById(int id);

    Post create(UserPostDTO userPostDTO);

    Post updatePost(int id, PostDTO postDTO);

    void deleteById(int id);
}
