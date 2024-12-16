package com.nguyenvannhat.library.controllers.comments;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.respones.CustomResponse;
import com.nguyenvannhat.library.services.comments.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @PreAuthorize("@roleChecker.hasPermission('" + "${api.v1.library.comment.read}" + "')")
    public CustomResponse<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return new CustomResponse<>(HttpStatus.OK, "Fetched all comments successfully", comments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@roleChecker.hasPermission('" + "${api.v1.library.comment.read}" + "')")
    public CustomResponse<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getComment(id);
        if (comment.isPresent()) {
            return new CustomResponse<>(HttpStatus.OK, "Comment found", comment.get());
        }
        return new CustomResponse<>(HttpStatus.NOT_FOUND, "Comment not found", null);
    }

    @GetMapping("/post/{postId}")
    @PreAuthorize("@roleChecker.hasPermission('" + "${api.v1.library.comment.read}" + "')")
    public CustomResponse<Comment> getCommentByPost(@PathVariable Long postId) {
        Post post = new Post();
        post.setId(postId);
        Comment comment = commentService.getCommentByPost(post);
        if (comment != null) {
            return new CustomResponse<>(HttpStatus.OK, "Comment found for the post", comment);
        }
        return new CustomResponse<>(HttpStatus.NOT_FOUND, "No comment found for the post", null);
    }

    @PostMapping
    @PreAuthorize("@roleChecker.hasPermission('" + "${api.v1.library.comment.create}" + "')")
    public CustomResponse<Comment> addComment(@RequestBody Comment comment) {
        Optional<Comment> newComment = commentService.addComment(comment);
        if (newComment.isPresent()) {
            return new CustomResponse<>(HttpStatus.CREATED, "Comment added successfully", newComment.get());
        }
        return new CustomResponse<>(HttpStatus.BAD_REQUEST, "Failed to add comment", null);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@roleChecker.hasPermission('" + "${api.v1.library.comment.update}" + "')")
    public CustomResponse<Comment> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        updatedComment.setId(id);
        Optional<Comment> comment = commentService.updateComment(updatedComment);
        if (comment.isPresent()) {
            return new CustomResponse<>(HttpStatus.OK, "Comment updated successfully", comment.get());
        }
        return new CustomResponse<>(HttpStatus.NOT_FOUND, "Comment not found", null);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@roleChecker.hasPermission('" + "${api.v1.library.comment.delete}" + "')")
    public CustomResponse<Void> deleteCommentById(@PathVariable Long id) {
        if (!commentService.getComment(id).isPresent()) {
            return new CustomResponse<>(HttpStatus.NOT_FOUND, "Comment not found", null);
        }
        commentService.deleteById(id);
        return new CustomResponse<>(HttpStatus.NO_CONTENT, "Comment deleted successfully", null);
    }

    @DeleteMapping
    @PreAuthorize("@roleChecker.hasPermission('" + "${api.v1.library.comment.delete}" + "')")
    public CustomResponse<Void> deleteComment(@RequestBody Comment comment) {
        if (comment == null || comment.getId() == null) {
            return new CustomResponse<>(HttpStatus.BAD_REQUEST, "Invalid comment object", null);
        }
        commentService.deleteComment(comment);
        return new CustomResponse<>(HttpStatus.NO_CONTENT, "Comment deleted successfully", null);
    }
}
