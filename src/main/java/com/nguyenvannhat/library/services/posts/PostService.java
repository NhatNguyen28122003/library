package com.nguyenvannhat.library.services.posts;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    Post getPostById(long id);
    List<PostDTO> getPosts();
    PostDTO findPostByUser(User user);
    PostDTO updatePost(PostDTO postDTO);
    PostDTO deletePost(PostDTO postDTO);
}
