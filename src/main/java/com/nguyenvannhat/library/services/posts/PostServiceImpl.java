package com.nguyenvannhat.library.services.posts;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.entities.UserPost;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.PostCommentRepository;
import com.nguyenvannhat.library.repositories.PostRepository;
import com.nguyenvannhat.library.repositories.UserPostRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import com.nguyenvannhat.library.responses.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final UserPostRepository userPostRepository;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private static final Locale locale = Locale.ENGLISH;

    @Override
    public CustomResponse<List<Post>> create(PostDTO postDTO) {
        if (postRepository.findByTitle(postDTO.getTitle()).isPresent()) {
            throw new ApplicationException(Constant.ERROR_POST_EXIST);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName()).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_USER_NOT_FOUND)
        );
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .body(postDTO.getBody())
                .build();
        post.setTotalLikes(0);
        post.setCreateBy(authentication.getName());
        post.setUpdateBy(authentication.getName());
        Post savePost = postRepository.save(post);
        userPostRepository.save(UserPost.builder()
                .postId(savePost.getId())
                .userId(user.getId())
                .build());
        return CustomResponse.success(HttpStatus.CREATED.value(),
                messageSource.getMessage(Constant.SUCCESS_POST_CREATED, null, locale),
                postRepository.findAll());
    }

    @Override
    public CustomResponse<List<Post>> findAll() {
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_POST_INFORMATION, null, locale),
                postRepository.findAll());
    }

    @Override
    public CustomResponse<Post> findByTitle(String title) {
        Post post = postRepository.findByTitle(title).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_POST_NOT_FOUND)
        );
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_POST_INFORMATION, null, locale),
                post);
    }

    @Override
    public CustomResponse<List<Post>> findByUser(User user) {
        List<Post> posts = postRepository.findPostsByUser(user);
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_POST_INFORMATION, null, locale),
                posts);
    }

    @Override
    public CustomResponse<List<Post>> update(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_POST_NOT_FOUND)
        );
        if (postRepository.findByTitle(postDTO.getTitle()).isEmpty()) {
            post.setTitle(postDTO.getTitle());
        }
        post.setBody(postDTO.getBody());
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_POST_INFORMATION, null, locale),
                postRepository.findAll());
    }

    @Override
    public CustomResponse<List<Post>> delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_POST_NOT_FOUND)
        );
        postCommentRepository.deleteAll(postCommentRepository.findByPost(post));
        userPostRepository.deleteAll(userPostRepository.findAllByPost(post));
        postRepository.delete(post);
        return null;
    }

    @Override
    public CustomResponse<List<Post>> likePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_POST_NOT_FOUND)
        );
        post.setTotalLikes(post.getTotalLikes() + 1);
        postRepository.save(post);
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_POST_INFORMATION, null, locale),
                postRepository.findAll());
    }

    @Override
    public CustomResponse<List<Post>> unlikePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_POST_NOT_FOUND)
        );
        if (post.getTotalLikes() > 0) {
            post.setTotalLikes(post.getTotalLikes() - 1);
        }
        return CustomResponse.success(HttpStatus.OK.value(),
                messageSource.getMessage(Constant.SUCCESS_POST_INFORMATION, null, locale),
                postRepository.findAll());
    }
}
