package com.nguyenvannhat.library.service;

import com.nguyenvannhat.library.dtos.UserPostDTO;
import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.models.Post;
import com.nguyenvannhat.library.models.User;
import com.nguyenvannhat.library.repositories.PostRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll().stream().toList();
    }

    @Override
    public Post findById(int id) {
        return postRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Post not found!")
        );
    }

    @Override
    public Post create(UserPostDTO userPostDTO) {
        Optional<User> optionalUser = userRepository.findByFullName(userPostDTO.getUserDTO().getName());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Don't create post");
        }
        Post newPost = Post.builder()
                .id(0)
                .title(userPostDTO.getPostDTO().getTitle())
                .description(userPostDTO.getPostDTO().getDescription())
                .user(optionalUser.get())
                .build();
        postRepository.save(newPost);
        return newPost;
    }

    @Override
    public Post updatePost(int id, PostDTO postDTO) {
        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isEmpty()){
            throw new RuntimeException("Khong the cap nhat duoc");
        }
        Post post = existingPost.get();
        if (!postDTO.getTitle().isEmpty()) {
            post.setTitle(post.getTitle());
            post.setUpdatedDate(new Date(System.currentTimeMillis()));
        }
        if (!postDTO.getDescription().isEmpty()) {
            post.setDescription(post.getDescription());
            post.setUpdatedDate(new Date(System.currentTimeMillis()));
        }
        postRepository.save(post);
        return post;
    }

    @Override
    public void deleteById(int id) {
        postRepository.deleteById(id);
    }
}
