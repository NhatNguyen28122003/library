package com.nguyenvannhat.library.controllers.posts;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.posts.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(name = "Post Management", description = "APIs for managing posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @PreAuthorize("fileRole()")
    @Operation(summary = "Create a new post", description = "Add a new post to the system.")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO) throws Exception {
        postService.createPost(postDTO);
        return CustomResponse.success(postDTO);
    }

    @GetMapping
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get all posts", description = "Retrieve a list of all posts.")
    public ResponseEntity<?> getAllPosts() {
        List<PostDTO> posts = postService.getPosts();
        return CustomResponse.success(posts);
    }

    @GetMapping("/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get a post by ID", description = "Retrieve a single post using its ID.")
    public ResponseEntity<?> getPostById(@PathVariable long id) throws Exception {
        Post post = postService.getPostById(id);
        return CustomResponse.success(post);
    }

    @GetMapping("/topPost")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get top posts", description = "Retrieve a limited number of top posts. Default limit is 5.")
    public ResponseEntity<?> getTopPosts(@RequestParam(defaultValue = "5") int limit) {
        List<PostDTO> topPosts = postService.getTopPosts(limit);
        return CustomResponse.success(topPosts);
    }

    @PutMapping("/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Update a post", description = "Update the details of an existing post using its ID.")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) throws Exception {
        postService.updatePost(id, postDTO);
        return CustomResponse.success(postDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Delete a post", description = "Delete a post from the system using its ID.")
    public ResponseEntity<?> deletePost(@PathVariable long id) {
        postService.deleteById(id);
        return CustomResponse.success(id);
    }
}
