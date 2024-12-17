package com.nguyenvannhat.library.controllers.posts;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO) throws Exception {
        postService.createPost(postDTO);
        return CustomResponse.success(postDTO);
    }

    @GetMapping
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> getAllPosts() throws Exception {
        List<PostDTO> posts = postService.getPosts();
        return CustomResponse.success(posts);
    }

    @GetMapping("/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> getPostById(@PathVariable long id) throws Exception {
        Post post = postService.getPostById(id);
        return CustomResponse.success(post);

    }

    @GetMapping("/topPost")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> getTopPosts(
            @RequestParam(defaultValue = "5") int limit
    ) throws Exception {
        List<PostDTO> topPosts = postService.getTopPosts(limit);
        return CustomResponse.success(topPosts);
    }


    @PutMapping("/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody PostDTO postDTO
    ) throws Exception {
        postService.updatePost(id, postDTO);
        return CustomResponse.success(postDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<?> deletePost(@PathVariable long id) {
        postService.deleteById(id);
        return CustomResponse.success(id);
    }
}
