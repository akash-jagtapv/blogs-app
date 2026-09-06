package com.blogs.app.service;

import com.blogs.app.dto.UpdatePostRequest;
import com.blogs.app.entity.Post;
import com.blogs.app.entity.User;
import com.blogs.app.exception.PostNotFoundException;
import com.blogs.app.exception.UnauthorizedActionException;
import com.blogs.app.mapper.PostMapper;
import com.blogs.app.repository.PostRepository;
import com.blogs.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void getPostById_whenPostExists_returnsPost() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostService postService = new PostService(postRepository, userRepository);
        Post result = postService.getPostById(1L);

        assertThat(result.getTitle()).isEqualTo("Test Post");
    }

    @Test
    void getPostById_whenPostDoesNotExist_throwsException() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        PostService postService = new PostService(postRepository, userRepository);

        assertThatThrownBy(() -> postService.getPostById(999L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void updatePost_whenRequestingUserIsNotAuthor_throwsUnauthorizedException() {
        User author = new User();
        author.setId(1L);

        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Original Title");
        existingPost.setAuthor(author);

        UpdatePostRequest updateRequest = new UpdatePostRequest();
        PostMapper.updateEntity(existingPost, updateRequest);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));

        PostService postService = new PostService(postRepository, userRepository);

        assertThatThrownBy(() -> postService.updatePost(1L, updateRequest, 2L))
                .isInstanceOf(UnauthorizedActionException.class);
    }

    @Test
    void updatePost_whenRequestingUserIsAuthor_updatesSuccessfully() {
        User author = new User();
        author.setId(1L);

        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Original Title");
        existingPost.setBody("Original Body");
        existingPost.setAuthor(author);

        UpdatePostRequest updateRequest = new UpdatePostRequest();
        PostMapper.updateEntity(existingPost, updateRequest);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        PostService postService = new PostService(postRepository, userRepository);

        Post result = postService.updatePost(1L, updateRequest, 1L);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getBody()).isEqualTo("Updated Body");
    }

    @Test
    void deletePost_whenRequestingUserIsNotAuthor_throwsUnauthorizedException() {
        User author = new User();
        author.setId(1L);

        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Original Title");
        existingPost.setAuthor(author);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));

        PostService postService = new PostService(postRepository, userRepository);

        assertThatThrownBy(() -> postService.deletePost(1L, 2L))
                .isInstanceOf(UnauthorizedActionException.class);
    }

    @Test
    void deletePost_whenRequestingUserIsAuthor_deletesSuccessfully() {
        User author = new User();
        author.setId(1L);

        Post existingPost = new Post();
        existingPost.setId(1L);
        existingPost.setTitle("Original Title");
        existingPost.setAuthor(author);

        when(postRepository.findById(1L)).thenReturn(Optional.of(existingPost));

        PostService postService = new PostService(postRepository, userRepository);

        postService.deletePost(1L, 1L);

        verify(postRepository).delete(existingPost);
    }
}
