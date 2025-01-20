package com.nguyenvannhat.library.services.posts;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.responses.CustomResponse;

import java.util.List;

public interface PostService {
    CustomResponse<List<Post>> create(PostDTO postDTO);

    CustomResponse<List<Post>> findAll();

    CustomResponse<Post> findByTitle(String title);

    CustomResponse<List<Post>> findByUser(User user);

    CustomResponse<List<Post>> update(Long id, PostDTO postDTO);

    CustomResponse<List<Post>> delete(Long id);

    CustomResponse<List<Post>> likePost(Long id);
    CustomResponse<List<Post>> unlikePost(Long id);

}
