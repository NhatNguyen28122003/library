package com.nguyenvannhat.library.controllers.posts;

import com.nguyenvannhat.library.components.AppConfig;
import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.UserCustom;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.responses.SuccessCode;
import com.nguyenvannhat.library.services.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final AppConfig appConfig;

    @PostMapping("/create")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<PostDTO>> createPost(@RequestBody PostDTO postDTO) {
        postService.createPost(postDTO);
        return CustomResponse.success(HttpStatus.CREATED, SuccessCode.POST_CREATED, appConfig.messageSource(), postDTO);
    }

    @GetMapping("/read")
    public ResponseEntity<CustomResponse<List<PostDTO>>> getAllPosts() {
        List<PostDTO> posts = postService.getPosts();
        return CustomResponse.success(HttpStatus.OK, SuccessCode.POST_INFORMATION, appConfig.messageSource(), posts);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<CustomResponse<Post>> getPostById(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.POST_INFORMATION, appConfig.messageSource(), post);
    }

    @GetMapping("/top")
    public ResponseEntity<CustomResponse<List<PostDTO>>> getTopPosts() {
        List<PostDTO> topPosts = postService.getTopPosts();
        return CustomResponse.success(HttpStatus.OK, SuccessCode.POST_TOP, appConfig.messageSource(), topPosts);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<PostDTO>> updatePost(@PathVariable("id") Long id, @RequestBody PostDTO postDTO) {
        postService.updatePost(id, postDTO);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.POST_UPDATED, appConfig.messageSource(), postDTO);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> deletePostById(@PathVariable("id") Long id) {
        postService.deleteById(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.POST_DELETED, appConfig.messageSource(), "Post with ID " + id + " has been deleted.");
    }

    @PostMapping("/like")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> likePost(@RequestBody PostDTO postDTO) {
        postService.likePost(postDTO);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.POST_LIKED, appConfig.messageSource(), "Post liked successfully.");
    }

    @PostMapping("/unlike")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<String>> unlikePost(@RequestBody PostDTO postDTO) {
        postService.unlikePost(postDTO);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.POST_UNLIKED, appConfig.messageSource(), "Post unliked successfully.");
    }

    @GetMapping("/search/user")
    @PreAuthorize("fileRole()")
    public ResponseEntity<CustomResponse<Post>> findPostByUser(@RequestBody UserCustom userCustom) {
        Post post = postService.findPostByUser(userCustom);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.POST_INFORMATION, appConfig.messageSource(), post);
    }
}
