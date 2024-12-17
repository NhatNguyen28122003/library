package com.nguyenvannhat.library.controllers.posts;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @PreAuthorize("@jwtUtils.hasPermission('" + "${api.v1.library.post.create}" + "')")
    public CustomResponse<?> createPost(@RequestBody PostDTO postDTO) {
        try {
            postService.createPost(postDTO);
            return new CustomResponse<>(HttpStatus.CREATED, "Post created successfully!", postDTO);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @GetMapping
    @PreAuthorize("@jwtUtils.hasPermission('" + "${api.v1.library.post.read}" + "')")
    public CustomResponse<?> getAllPosts() {
        try {
            List<PostDTO> posts = postService.getPosts();
            return new CustomResponse<>(HttpStatus.OK, "Fetched all posts successfully!", posts);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@jwtUtils.hasPermission('" + "${api.v1.library.post.read}" + "')")
    public CustomResponse<?> getPostById(@PathVariable long id) {
        try {
            Post post = postService.getPostById(id);
            return new CustomResponse<>(HttpStatus.OK, "Post found!", new PostDTO(post.getTitle(), post.getBody()));
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @GetMapping("/topPost")
    @PreAuthorize("@jwtUtils.hasPermission('" + "${api.v1.library.post.read}" + "')")
    public CustomResponse<List<PostDTO>> getTopPosts(@RequestParam(defaultValue = "5") int limit) {
        try {
            List<PostDTO> topPosts = postService.getTopPosts(limit);
            return new CustomResponse<>(HttpStatus.OK, "Top posts fetched successfully!", topPosts);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("@jwtUtils.hasPermission('" + "${api.v1.library.post.update}" + "')")
    public CustomResponse<?> updatePost(@PathVariable long id, @RequestBody PostDTO postDTO) {
        try {
            postService.updatePost(id, postDTO);
            return new CustomResponse<>(HttpStatus.OK, "Post updated successfully!", postDTO);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@jwtUtils.hasPermission('" + "${api.v1.library.post.delete}" + "')")
    public CustomResponse<?> deletePost(@PathVariable long id) {
        try {
            postService.deleteById(id);
            return new CustomResponse<>(HttpStatus.NO_CONTENT, "Post deleted successfully!", null);
        } catch (Exception e) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }
}
