package com.nguyenvannhat.library.services.posts;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.exceptions.DataNotFoundException;

import java.util.List;

public interface PostService {
    void createPost(PostDTO postDTO) throws DataNotFoundException;

    Post getPostById(Long id) throws DataNotFoundException;

    List<PostDTO> getPosts();

    Post findPostByUser(User user);

    void updatePost(Long id, PostDTO postDTO) throws DataNotFoundException;

    void deletePost(PostDTO postDTO);


}
