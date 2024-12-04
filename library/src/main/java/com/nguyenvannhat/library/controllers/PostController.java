package com.nguyenvannhat.library.controllers;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.dtos.UserPostDTO;
import com.nguyenvannhat.library.models.Post;
import com.nguyenvannhat.library.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;


    @GetMapping("")
    public ResponseEntity<List<PostDTO>> getAllPost() {
        return ResponseEntity.ok(
                postService.getAllPosts().stream()
                        .map(post -> new PostDTO(post.getTitle(), post.getDescription())) // Giả sử PostDTO có constructor nhận Post
                        .collect(Collectors.toList())
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody UserPostDTO userPostDTO) {
        postService.create(userPostDTO);
        return ResponseEntity.ok("New post created successfully!!!");
    }
}
