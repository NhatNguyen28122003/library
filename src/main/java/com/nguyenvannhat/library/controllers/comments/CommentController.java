package com.nguyenvannhat.library.controllers.comments;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.services.comments.CommentService;
import com.nguyenvannhat.library.services.posts.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Tag(name = "Comment Management", description = "APIs for managing comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @GetMapping("/read")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get all comments", description = "Retrieve a list of all comments.")
    public CustomResponse<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return new CustomResponse<>(HttpStatus.OK, "Fetched all comments successfully", comments);
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get a comment by ID", description = "Retrieve a comment using its unique ID.")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) throws Exception {
        Comment comment = commentService.getCommentById(id);
        return CustomResponse.success(comment);
    }

    @GetMapping("/read/post/{postId}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Get comments by Post ID", description = "Retrieve all comments associated with a specific post ID.")
    public ResponseEntity<?> getCommentByPost(@PathVariable Long postId) throws Exception {
        Post post = postService.getPostById(postId);
        List<Comment> comments = commentService.getCommentByPost(post);
        return CustomResponse.success(comments);
    }

    @PostMapping("/create")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Create a new comment", description = "Add a new comment to the system.")
    public ResponseEntity<?> addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
        return CustomResponse.success(comment);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Update a comment", description = "Update an existing comment's details.")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        commentService.updateComment(updatedComment);
        return CustomResponse.success(updatedComment);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Delete a comment by ID", description = "Delete a comment using its unique ID.")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id) {
        commentService.deleteById(id);
        return CustomResponse.success(id);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("fileRole()")
    @Operation(summary = "Delete a comment", description = "Delete a specific comment entity provided in the request body.")
    public ResponseEntity<?> deleteComment(@RequestBody Comment comment) {
        commentService.deleteComment(comment);
        List<Comment> comments = commentService.getAllComments();
        return CustomResponse.success(comments);
    }
}
