package com.nguyenvannhat.library.controller;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.posts.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    // Endpoint để tạo bài đăng mới
    @PreAuthorize("fileRole(#request)")
    @PostMapping("/create")
    public CustomResponse<List<Post>> createPost(HttpServletRequest request, @RequestBody PostDTO postDTO) {
        return postService.create(postDTO);
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read")
    public CustomResponse<List<Post>> getAllPosts(HttpServletRequest request) {
        return postService.findAll();
    }

    @PreAuthorize("fileRole(#request)")
    @GetMapping("/read/{title}")
    public CustomResponse<Post> getPostByTitle(HttpServletRequest request, @PathVariable String title) {
        return postService.findByTitle(title);
    }


    @PreAuthorize("fileRole(#request)")
    @PutMapping("/{id}")
    public CustomResponse<List<Post>> updatePost(HttpServletRequest request, @PathVariable Long id, @RequestBody PostDTO postDTO) {
        return postService.update(id, postDTO);
    }

    @PreAuthorize("fileRole(#request)")
    @PostMapping("/like/{id}")
    public CustomResponse<List<Post>> likePost(HttpServletRequest request, @PathVariable Long id) {
        return postService.likePost(id);
    }

    @PreAuthorize("fileRole(#request)")
    @PostMapping("/unlike/{id}")
    public CustomResponse<List<Post>> unlikePost(HttpServletRequest request,@PathVariable Long id) {
        return postService.unlikePost(id);
    }
}
