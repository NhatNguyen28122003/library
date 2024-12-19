package com.nguyenvannhat.library.services.posts;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.entities.UserPost;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.exceptions.ErrorCode;
import com.nguyenvannhat.library.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserPostRepository userPostRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserCommentRepository userCommentRepository;

    @Override
    public void createPost(PostDTO postDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new ApplicationException(ErrorCode.USER_NOT_FOUND)
        );
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .body(postDTO.getBody())
                .totalLikes(0)
                .build();
        post.setCreateBy(user.getFullName());
        post.setUpdateBy(user.getFullName());
        Long postId = postRepository.save(post).getId();
        UserPost userPost = UserPost.builder()
                .postId(postId)
                .userId(user.getId())
                .build();
        userPostRepository.save(userPost);
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }


    @Override
    public List<PostDTO> getPosts() {
        return postRepository.findAll().stream().map(
                post -> new PostDTO(post.getTitle(), post.getBody())
        ).collect(Collectors.toList());
    }

    @Override
    public Post findPostByUser(User user) {
        return postRepository.findPostByUser(user);
    }

    @Override
    public void updatePost(Long id, PostDTO postDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new ApplicationException(ErrorCode.USER_NOT_FOUND)
        );
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.POST_NOT_FOUND)
        );
        post.setTitle(postDTO.getTitle());
        post.setBody(postDTO.getBody());
        post.setUpdateBy(user.getFullName());
        postRepository.save(post);
    }

    @Override
    public void deletePost(PostDTO postDTO) {
        Post post = postRepository.findPostByTitle(postDTO.getTitle());
        if (post == null) {
            return;
        }
        userCommentRepository.deleteByPostId(post.getId());
        postCommentRepository.deleteByPostId(post.getId());
        userPostRepository.deleteByPostId(post.getId());
        postRepository.deleteById(post.getId());
    }

    @Override
    public void likePost(PostDTO postDTO) {
        Post post = postRepository.findPostByTitle(postDTO.getTitle());
        if (post == null) {
            return;
        }
        post.setTotalLikes(post.getTotalLikes() + 1);
        postRepository.save(post);
    }

    @Override
    public void unlikePost(PostDTO postDTO) {
        Post post = postRepository.findPostByTitle(postDTO.getTitle());
        if (post == null || post.getTotalLikes() == 0) {
            return;
        }
        post.setTotalLikes(post.getTotalLikes() - 1);
        postRepository.save(post);
    }

    @Override
    public List<PostDTO> getTopPosts() {
        return postRepository.getTopLikedPosts()
                .stream()
                .map(
                        post -> new PostDTO(post.getTitle(), post.getBody())
                ).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        if (postRepository.findById(id).isPresent()) {
            userCommentRepository.deleteByPostId(id);
            postCommentRepository.deleteByPostId(id);
            userPostRepository.deleteByPostId(id);
            postRepository.deleteById(id);
        }
    }
}
