package com.nguyenvannhat.library.services.posts;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.CommentDTO;
import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.dtos.requests.PostRequest;
import com.nguyenvannhat.library.entities.Comment;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.PostComment;
import com.nguyenvannhat.library.entities.UserComment;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.CommentRepository;
import com.nguyenvannhat.library.repositories.PostCommentRepository;
import com.nguyenvannhat.library.repositories.PostRepository;
import com.nguyenvannhat.library.repositories.UserCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final CommentRepository commentRepository;
    private final UserCommentRepository userCommentRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostDTO create(PostRequest request) {
        Optional<Post> existing = postRepository.findByCode(request.getCode().toLowerCase());
        if (existing.isPresent()) {
            log.error("Post with code {} already exists", request.getCode());
            throw new ApplicationException(Constant.ERROR_POST_EXIST);
        }

        Post newPost = Post.builder()
                .code(request.getCode().toLowerCase())
                .title(request.getTitle())
                .body(request.getBody())
                .totalLikes(0)
                .disLikes(0)
                .build();
        newPost = postRepository.save(newPost);
        return modelMapper.map(newPost, PostDTO.class);
    }

    @Override
    public PostDTO update(Long id, PostRequest request) {
        return null;
    }

    @Override
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> modelMapper.map(post, PostDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getComments(Long postId) {
        List<Comment> comments = postRepository.findCommentsByPost(postId);
        return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).collect(Collectors.toList());
    }

    @Override
    public PostDTO likePost(Post post) {
        Integer totalLikes = post.getTotalLikes() + 1;
        post.setTotalLikes(totalLikes);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO unlikePost(Post post) {
        if (post.getTotalLikes().compareTo(Integer.valueOf(0)) == 1) {
            post.setTotalLikes(post.getTotalLikes() - 1);
            post = postRepository.save(post);
        }
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO delete(Post post) {
        List<PostComment> pc = postCommentRepository.findByPost(post);
        List<Long> commentIds = pc.stream().map(PostComment::getId).collect(Collectors.toList());
        List<Comment> comments = commentRepository.findByIdIn(commentIds);
        List<UserComment> userComments = userCommentRepository.findByCommentIdIn(commentIds);
        postCommentRepository.deleteAll(pc);
        commentRepository.deleteAll(comments);
        userCommentRepository.deleteAll(userComments);
        return null;
    }
}
