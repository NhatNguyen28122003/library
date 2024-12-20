package com.nguyenvannhat.library.controllers.comments;

import com.nguyenvannhat.library.components.AppConfig;
import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.responses.CustomResponse;
import com.nguyenvannhat.library.responses.SuccessCode;
import com.nguyenvannhat.library.services.comments.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AppConfig appConfig;

    @GetMapping
    public ResponseEntity<CustomResponse<List<Comment>>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return CustomResponse.success(HttpStatus.OK, SuccessCode.COMMENT_RETRIEVED, appConfig.messageSource(), comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Comment>> getCommentById(@PathVariable Long id) throws Exception {
        Comment comment = commentService.getCommentById(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.COMMENT_RETRIEVED, appConfig.messageSource(), comment);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<CustomResponse<List<Comment>>> getCommentsByPost(@PathVariable Long postId) {
        Post post = new Post();
        post.setId(postId);
        List<Comment> comments = commentService.getCommentByPost(post);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.COMMENT_RETRIEVED, appConfig.messageSource(), comments);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomResponse<Comment>> addComment(@RequestBody Comment comment) {
        Optional<Comment> savedComment = commentService.addComment(comment);
        return CustomResponse.success(HttpStatus.CREATED, SuccessCode.COMMENT_CREATED, appConfig.messageSource(), savedComment.orElseThrow());
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomResponse<Comment>> updateComment(@RequestBody Comment comment) {
        Optional<Comment> updatedComment = commentService.updateComment(comment);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.COMMENT_UPDATED, appConfig.messageSource(), updatedComment.orElseThrow());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomResponse<String>> deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return CustomResponse.success(HttpStatus.OK, SuccessCode.COMMENT_DELETED, appConfig.messageSource(), "Comment with ID " + id + " has been deleted.");
    }
}
