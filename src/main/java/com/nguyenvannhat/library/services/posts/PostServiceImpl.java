package com.nguyenvannhat.library.services.posts;

import com.nguyenvannhat.library.dtos.PostDTO;
import com.nguyenvannhat.library.entities.Post;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.exceptions.DataNotFoundException;
import com.nguyenvannhat.library.repositories.PostRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
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

    @Override
    public void createPost(PostDTO postDTO) throws DataNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new DataNotFoundException("User not found")
        );
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .body(postDTO.getBody())
                .totalLikes(0)
                .build();
        post.setCreateBy(user.getFullName());
        post.setUpdateBy(user.getFullName());
        postRepository.save(post);
    }

    @Override
    public Post getPostById(Long id) throws DataNotFoundException {
        return postRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Post not found with id: " + id));
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
    public void updatePost(Long id, PostDTO postDTO) throws DataNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new DataNotFoundException("User not found!!!")
        );
        Post post = postRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Post not found with id: " + id)
        );
        post.setTitle(postDTO.getTitle());
        post.setBody(postDTO.getBody());
        post.setUpdateBy(user.getFullName());
        postRepository.save(post);
    }

    @Override
    public void deletePost(PostDTO postDTO) {
        if (postRepository.findPostByTitle(postDTO.getTitle()) == null) {
            return;
        }
        postRepository.delete(postRepository.findPostByTitle(postDTO.getTitle()));
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
    public List<PostDTO> getTopPosts(int limit) {
        return postRepository.findAll()
                .stream()
                .sorted((post1, post2) -> Integer.compare(post2.getTotalLikes(), post1.getTotalLikes()))
                .limit(limit)
                .map(post -> new PostDTO(post.getTitle(), post.getBody()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        if (postRepository.findById(id).isPresent()) {
            postRepository.deleteById(id);
        }
    }
}
