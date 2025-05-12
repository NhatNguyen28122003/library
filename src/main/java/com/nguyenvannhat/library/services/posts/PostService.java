package com.nguyenvannhat.library.services.posts;

import com.nguyenvannhat.library.dtos.CommentDTO;
import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.dtos.requests.PostRequest;
import com.nguyenvannhat.library.entities.Post;

import java.util.List;

public interface PostService {
    PostDTO create(PostRequest request);
    PostDTO update(Long id, PostRequest request);
    List<PostDTO> getAllPosts();
    List<CommentDTO> getComments(Long pos);
    PostDTO likePost(Post post);
    PostDTO unlikePost(Post post);
    PostDTO delete(Post post);
}
