package com.nguyenvannhat.library.controllers.comments;

import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.services.comments.CommentService;
import com.nguyenvannhat.library.services.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    
}
