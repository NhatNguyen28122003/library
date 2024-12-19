package com.nguyenvannhat.library.services.posts;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;

import java.util.List;

public interface PostService {
    void createPost(PostDTO postDTO) throws Exception;

    Post getPostById(Long id) throws Exception;

    List<PostDTO> getPosts();

    Post findPostByUser(User user);

    void updatePost(Long id, PostDTO postDTO) throws Exception;

    void deletePost(PostDTO postDTO);
    void deleteById(Long id);

    void likePost(PostDTO postDTO);

    void unlikePost(PostDTO postDTO);
    List<PostDTO> getTopPosts();


}
